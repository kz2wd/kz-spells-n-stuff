package com.cludivers.kz2wdprison.gameplay.player

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import com.cludivers.kz2wdprison.gameplay.world.cuboid.Cuboid
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
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