package com.cludivers.kz2wdprison.modules.player

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.apache.maven.building.Source
import org.bukkit.entity.Player

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
    this.sendMessage(Component.text(text).color(NamedTextColor.RED))
}

fun Player.sendSuccessMessage(text: String) {
    this.sendMessage(Component.text(text).color(NamedTextColor.GREEN))
}

fun Player.sendNotificationMessage(text: String) {
    this.sendMessage(Component.text(text).color(NamedTextColor.YELLOW))
}

fun Player.sendConfirmationMessage(text: String, onAccept: String, onRefuse: String){
    val msg = Component.text(text)

    val acceptButton = Component.text("Accepter").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)
    val acceptClick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, onAccept)
    val acceptHover = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Accepter"))

    val refuseButton = Component.text("Refuser").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
    val refuseCLick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, onRefuse)
    val refuseHover = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Refuser"))

     this.sendMessage(msg
         .appendNewline().append(acceptButton.clickEvent(acceptClick).hoverEvent(acceptHover))
         .appendSpace().append(refuseButton.clickEvent(refuseCLick).hoverEvent(refuseHover)))
}

fun Player.requestShards(args: Array<String>, index: Int): Int? {
    if (args.size < index + 1) {
        this.sendErrorMessage("Vous devez préciser une quantité de fragments à ajouter.")
        return null
    }

    val shardAmount = try {
        args[index].toInt()
    } catch (e: NumberFormatException) {
        this.sendErrorMessage("Quantité invalide : ${args[1]}.")
        return null
    }

    if (this.getData().shards < shardAmount) {
        this.sendErrorMessage("Vous n'avez pas assez de fragments.")
        return null
    }

    return shardAmount
}

fun Player.notifyPlayer(notification: Component, sound: Sound) {
    this.sendActionBar(notification)
    this.playSound(sound)
}

fun Player.giveShards(shards: Double) {
    PluginConfiguration.session.beginTransaction()
    this.getData().shards += shards
    PluginConfiguration.session.transaction.commit()
    val sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 0.5f, 1f)
    this.notifyPlayer(Component.text("+${shards.toInt()} (${this.getData().shards.toInt()})").color(NamedTextColor.GREEN), sound)
}

fun Player.tryTakeShards(shards: Double): Boolean {
    if (this.getData().shards < shards) { return false }
    PluginConfiguration.session.beginTransaction()
    this.getData().shards -= shards
    PluginConfiguration.session.transaction.commit()
    val sound = Sound.sound(org.bukkit.Sound.BLOCK_DISPENSER_FAIL, Sound.Source.PLAYER, 0.5f, 1f)
    this.notifyPlayer(Component.text("-${shards.toInt()} (${this.getData().shards.toInt()})").color(NamedTextColor.RED), sound)
    return true
}

