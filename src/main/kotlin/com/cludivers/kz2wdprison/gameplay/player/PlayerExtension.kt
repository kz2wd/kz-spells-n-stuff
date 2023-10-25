package com.cludivers.kz2wdprison.gameplay.player

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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

