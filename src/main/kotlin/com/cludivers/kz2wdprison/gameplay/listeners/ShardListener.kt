package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hibernate.Session

class ShardListener(private val session: Session) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val transaction = session.beginTransaction()
        val playerData = event.player.getData(session)
        if (playerData.connectionAmount < 1) {
            Bukkit.broadcast(Component.text("Bienvenue à ${ChatColor.LIGHT_PURPLE}${event.player.name} ${ChatColor.WHITE} !"))
        } else {
            event.player.sendMessage(Component.text("Rebonjour ${ChatColor.LIGHT_PURPLE}${event.player.name}"))
        }
        playerData.connectionAmount += 1

        playerData.recomputePlayerIntrinsic(event.player)

        transaction.commit()

        // Set resource pack
        // event.player.setResourcePack("https://drive.google.com/uc?export=download&id=1VFzPQhxXNI_nk4dYYLVqazzHl_eEifND")

    }

}