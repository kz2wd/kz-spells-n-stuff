package com.cludivers.kz2wdprison.gameplay.player

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.ChunkBean
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.NationBean
import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import com.cludivers.kz2wdprison.gameplay.nation.NationInvitation
import com.cludivers.kz2wdprison.gameplay.nation.NationInvitation.addInvitation
import com.cludivers.kz2wdprison.gameplay.nation.NationInvitation.removeInvitation
import com.cludivers.kz2wdprison.gameplay.nation.NationInvitation.sendInvitationNotification
import com.cludivers.kz2wdprison.gameplay.world.cuboid.Cuboid
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.ChatColor
import org.bukkit.entity.Player


fun Player.getData(): PlayerBean {

    var playerData = PluginConfiguration.session
        .createQuery("from PlayerBean P where P.uuid = :uuid", PlayerBean::class.java)
        .setParameter("uuid", this.uniqueId.toString())
        .uniqueResult()

    if (playerData == null) {
        playerData = PlayerBean()
        playerData.uuid = this.uniqueId.toString()
        PluginConfiguration.session.persist(playerData)
    }

    return playerData
}
fun Player.isInArea(cuboid: Cuboid): Boolean{
    return cuboid.start.x <= this.location.x && this.location.x <= cuboid.end.x &&
            cuboid.start.y <= this.location.y && this.location.y <= cuboid.end.y &&
            cuboid.start.z <= this.location.z && this.location.z <= cuboid.end.z
}

fun Player.bossBarDisplay(text: Component, progress: Float, color: BossBar.Color = BossBar.Color.PURPLE,
                          overlay: BossBar.Overlay = BossBar.Overlay.PROGRESS){
    val playerTransientData = PlayerTransientData.getPlayerTransientData(this)

    if (playerTransientData.currentBossBarDisplay !is BossBar){
        val bossBar: BossBar = BossBar.bossBar(text, progress, color, overlay)
        playerTransientData.currentBossBarDisplay = bossBar
        this.showBossBar(playerTransientData.currentBossBarDisplay!!)
    }
    playerTransientData.currentBossBarDisplay!!.name(text).progress(progress).color(color).overlay(overlay)
}

fun Player.sendErrorMessage(text: String) {
    this.sendMessage(Component.text(ChatColor.RED.toString() + text))
}
fun Player.sendSuccesMessage(text: String) {
    this.sendMessage(Component.text(ChatColor.GREEN.toString() + text))
}

fun Player.sendNotificationMessage(text: String) {
    this.sendMessage(Component.text(ChatColor.YELLOW.toString() + text))
}

fun Player.sendConfirmationMessage(text: String, onAccept: String, onRefuse: String){
    val msg = Component.text(text)

    val acceptButton = Component.text("${ChatColor.GREEN}${ChatColor.BOLD}Accepter")
    val acceptClick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, onAccept)
    val acceptHover = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Accepter"))

    val refuseButton = Component.text("${ChatColor.RED}${ChatColor.BOLD}Refuser")
    val refuseCLick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, onRefuse)
    val refuseHover = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Refuser"))

     this.sendMessage(msg
         .appendNewline().append(acceptButton.clickEvent(acceptClick).hoverEvent(acceptHover))
         .appendSpace().append(refuseButton.clickEvent(refuseCLick).hoverEvent(refuseHover)))
}

fun Player.createNation() {
    val playerData = this.getData()
    if (playerData.nation is NationBean) {
        this.sendErrorMessage("Vous appartenez déjà à une nation")
        return
    }

    PluginConfiguration.session.beginTransaction()

    val nation = NationBean.instantiateAndPersistDefaultNation(playerData, "Nation de ${this.name}")

    playerData.nation = nation

    PluginConfiguration.session.transaction.commit()
    this.sendSuccesMessage("Votre nation vient d'être créée !")
}

fun Player.invitePlayerToNation(invitedPlayer: Player) {

    val senderData = this.getData()
    if (senderData.nation !is NationBean) {
        this.sendErrorMessage("Vous n'appartenez à aucune nation")
        return
    }
    if (invitedPlayer == this) {
        this.sendErrorMessage("Vous essayez de vous inviter vous-même ?")
        return
    }
    if (senderData.nation == invitedPlayer.getData().nation) {
        this.sendErrorMessage("${invitedPlayer.name} est déjà dans votre nation")
        return
    }

    if (NationInvitation.doInvitationExist(this, invitedPlayer)){
        this.sendErrorMessage("Vous avez déjà invité ${invitedPlayer.name}");
        return
    }
    addInvitation(this, invitedPlayer, senderData.nation!!)
    sendInvitationNotification(this, invitedPlayer, senderData.nation!!)
    this.sendSuccesMessage("Invitation envoyée à ${invitedPlayer.name}");
}

fun Player.acceptNationInvitation(playerInviting: Player) {

    val playerData = this.getData()

    if (playerData.nation is NationBean) {
        this.sendErrorMessage("Vous appartenez déjà à une nation, quittez la pour en rejoindre une autre")
        return
    }

    val nation = removeInvitation(playerInviting, this)
    if (nation !is NationBean) {
        this.sendErrorMessage("Vous n'avez pas été invité par cette personne")
        return
    }

    PluginConfiguration.session.beginTransaction()
    nation.addMember(playerData)
    PluginConfiguration.session.transaction.commit()
    this.sendSuccesMessage("Vous avez rejoint la nation ${nation.name}")
}

fun Player.refuseNationInvitation(playerInviting: Player){

    val nation = removeInvitation(playerInviting, this)
    if (nation !is NationBean){
        this.sendErrorMessage("Cette personne ne vous à pas invitée")
        return
    }
    this.sendSuccesMessage("Vous avez refusé l'invitation à rejoindre la nation : ${nation.name}")
}

fun Player.quitNation() {
    val playerData = this.getData()
    val nation = playerData.nation
    if (nation !is NationBean) {
        this.sendErrorMessage("Vous n'appartenez à aucune nation")
        return
    }
    PluginConfiguration.session.beginTransaction()
    nation.removeMember(playerData)

    if (nation.owner == playerData) {
        val newOwner = nation.findNewOwner()
        if (newOwner !is PlayerBean){
            nation.delete()
            this.sendSuccesMessage("Votre nation à été supprimée")
        } else {
            nation.changeOwner(newOwner)

        }

    }
    PluginConfiguration.session.transaction.commit()
    this.sendSuccesMessage("Vous avez quitté votre nation")
}

fun Player.claimChunk() {
    val playerData = this.getData()
    val playerNation = playerData.nation
    if (playerNation !is NationBean) {
        this.sendErrorMessage("Vous n'appartenez à aucune nation")
        return
    }

    if (!(playerData == playerNation.owner || playerData.permissionGroup!!.nationPermission!!.canClaim)) {
        this.sendErrorMessage("Vous n'avez pas la permission pour vous approprier des chunks")
        return
    }

    val chunkData = ChunkBean.getChunkBean(this.chunk)
    if (chunkData.nation is NationBean){
        this.sendErrorMessage("Ce chunk n'est pas disponible")
        return
    }

    if (playerNation.chunkClaimTokens!! <= 0){
        this.sendErrorMessage("Vous n'avez plus de jeton d'appropriation de chunk")
        return
    }

    PluginConfiguration.session.beginTransaction()

    playerNation.chunkClaimTokens = playerNation.chunkClaimTokens!! - 1
    playerNation.chunks!!.add(chunkData)
    chunkData.nation = playerNation
    PluginConfiguration.session.transaction.commit()

    this.sendSuccesMessage("Le chunk à bien été approprié")

}

