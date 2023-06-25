package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import com.cludivers.kz2wdprison.gameplay.player.getData
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Server
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.log10

class ShardListener(private val server: Server, private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val transaction = HibernateSession.session.beginTransaction()
        val playerData = event.player.getData()
        if (playerData.connectionAmount < 1) {
            Bukkit.broadcast(Component.text("Bienvenue à ${ChatColor.LIGHT_PURPLE}${event.player.name} ${ChatColor.WHITE} !"))
        } else {
            event.player.sendMessage(Component.text("Rebonjour ${ChatColor.LIGHT_PURPLE}${event.player.name}"))
        }
        playerData.connectionAmount += 1

        transaction.commit()

        // Set resource pack
        // event.player.setResourcePack("https://drive.google.com/uc?export=download&id=1VFzPQhxXNI_nk4dYYLVqazzHl_eEifND")

    }

    private fun calculateScaledHealth(health: Double): Double {

        // It looks like you could tweak those, but don't, it will break the curve
        val maxDisplayHealth = 40.0
        val linearRange = 10.0

        return if (health < linearRange) {
            health
        } else if (health >= 100) {
            maxDisplayHealth
        } else {
            val logInput = health + 1 - linearRange / 2
            val logOutput = log10(logInput) / log10(100 + 1 - linearRange / 2)
            val scaledHealth = logOutput * (maxDisplayHealth + linearRange) - linearRange
            scaledHealth.coerceAtMost(maxDisplayHealth)
        }
    }

    private fun updatePlayerHealthScaling(player: Player) {
        Bukkit.broadcast(Component.text("Vie max: ${player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value}"))
        player.healthScale = calculateScaledHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
        player.sendHealthUpdate()
    }

    @EventHandler
    fun onPlayerEquipArmor(event: PlayerArmorChangeEvent) {
//        server.scheduler.runTaskLater(plugin, Runnable {  }, 20L)
        updatePlayerHealthScaling(event.player)
    }

}