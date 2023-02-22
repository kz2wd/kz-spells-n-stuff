package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.world.cuboid.Cuboid
import com.cludivers.kz2wdprison.player.PlayerTransientData
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

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