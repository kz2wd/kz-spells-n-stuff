package com.cludivers.kz2wdprison.gameplay.player

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import com.cludivers.kz2wdprison.framework.beans.nation.NationBean
import com.cludivers.kz2wdprison.gameplay.nation.NationInvitation
import com.cludivers.kz2wdprison.gameplay.world.cuboid.Cuboid
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.hibernate.Session


fun Player.getData(session: Session): PlayerBean {
    var playerData = session
        .createQuery("from PlayerBean P where P.uuid = :uuid", PlayerBean::class.java)
        .setParameter("uuid", this.uniqueId.toString())
        .uniqueResult()

    if (playerData == null){
        playerData = PlayerBean()
        playerData.uuid = this.uniqueId.toString()
        session.persist(playerData)
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

fun Player.createNation(session: Session){
    val playerData = this.getData(session)
    if (playerData.nation is NationBean){
        this.sendErrorMessage("Vous appartenez déjà à une nation")
        return
    }

    session.beginTransaction()

    val nation = NationBean.instantiateAndPersistDefaultNation(session, playerData, "Nation de ${this.name}")

    playerData.nation = nation

    session.transaction.commit()
    this.sendSuccesMessage("Votre nation vient d'être créée !")
}

fun Player.invitePlayerToNation(session: Session, invitedPlayer: Player){

    val senderData = this.getData(session)
    if (senderData.nation !is NationBean){
        this.sendErrorMessage("Vous n'appartenez à aucune nation")
        return
    }

    if (NationInvitation.doInvitationExist(this, invitedPlayer)){
        this.sendErrorMessage("Vous avez déjà invité ce joueur");
        return
    }
    NationInvitation.addInvitation(this, invitedPlayer, senderData.nation!!)
    this.sendSuccesMessage("Invitation envoyée à ${invitedPlayer.name}");
}

fun Player.acceptNationInvitation(session: Session, playerInviting: Player){

    val playerData = this.getData(session)

    if (playerData.nation is NationBean){
        this.sendErrorMessage("Vous appartenez déjà à une nation, quittez la pour en rejoindre une autre")
        return
    }

    val nation = NationInvitation.removeInvitation(playerInviting, this)
    if (nation !is NationBean){
        this.sendErrorMessage("Vous n'avez pas été invité par cette personne")
        return
    }

    session.beginTransaction()
    playerData.nation = nation
    session.transaction.commit()
    this.sendSuccesMessage("Vous avez rejoint la nation ${nation.name}")

}

fun Player.quitNation(session: Session){
    val playerData = this.getData(session)
    val nation = playerData.nation
    if (nation !is NationBean){
        this.sendErrorMessage("Vous n'appartenez à aucune nation")
        return
    }
    session.beginTransaction()
    if (nation.owner == playerData){
        val newOwner = nation.findNewOwner()
        if (newOwner !is PlayerBean){
            session.remove(nation)
            this.sendSuccesMessage("Votre nation à été supprimée")
        } else {
            nation.changeOwner(newOwner)
        }
    }
    nation.removeMember(playerData)
    playerData.nation = null
    session.transaction.commit()
    this.sendSuccesMessage("Vous avez quitté votre nation")
}

