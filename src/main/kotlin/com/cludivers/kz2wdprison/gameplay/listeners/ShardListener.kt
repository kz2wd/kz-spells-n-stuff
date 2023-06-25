package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import com.cludivers.kz2wdprison.gameplay.player.getData
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.math.log10

class ShardListener() : Listener {

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

    private fun getHealthScale(health: Double): Double {
        val maxDisplayHealth = 20.0
        return log10(health + 1) / log10(maxDisplayHealth + 1) * maxDisplayHealth
    }

    private fun updatePlayerHealthScaling(player: Player) {
        player.healthScale = getHealthScale(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
        player.sendHealthUpdate()
    }

    @EventHandler
    fun onPlayerEquipArmor(event: PlayerArmorChangeEvent) {
        updatePlayerHealthScaling(event.player)
    }

    @EventHandler
    fun onPlayerGainHealth(event: EntityRegainHealthEvent) {
        if (event.entity !is Player) {
            return
        }
        updatePlayerHealthScaling(event.entity as Player)
    }

    @EventHandler
    fun onPlayerLoseHealth(event: EntityDamageEvent) {
        if (event.entity !is Player) {
            return
        }
        updatePlayerHealthScaling(event.entity as Player)

    }

}