package com.cludivers.kz2wdprison.modules.shards.listeners

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.framework.utils.Utils
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.log10

class ShardListener(private val server: Server, private val plugin: JavaPlugin) : Listener {

    private fun connectionWarning() {
        if (PluginConfiguration.isDatabaseConnected) return
        Bukkit.broadcast(
            Component.text("Aucune connexion à une base de donnée [Pas de persistance effectuée]")
                .color(NamedTextColor.RED)
        )

    }

    private fun notInProductionMessage(player: Player) {
        if (PluginConfiguration.isInProduction) return
        player.sendMessage(Component.text("[MODE DE DEVELOPPEMENT] Si vous découvrez ce plugin, essayer les commandes suivantes, en clickant dessus :"))
        player.sendMessage(
            Utils.createClickableCommandReference(
                "/artifact enable_resourcepack",
                ": Active le pack de texture."
            )
        )
        player.sendMessage(
            Utils.createClickableCommandReference(
                "/artifact demo",
                ": Ouvre un inventaire rempli d'artefacts de test."
            )
        )
        player.sendMessage(
            Utils.createClickableCommandReference(
                "/artifact runes",
                ": Ouvre un inventaire rempli de runes d'artefact."
            )
        )
        player.sendMessage(Utils.createClickableCommandReference("/nation create", ": Créer une nation à votre nom."))
        player.sendMessage(
            Utils.createClickableCommandReference(
                "/nation claim",
                ": Attribue le chunk courant à votre nation (nécessaire pour constuire)."
            )
        )
    }

    private fun playerNameFancy(player: Player): Component {
        return Component.text(player.name).color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.ITALIC)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val transaction = PluginConfiguration.session.beginTransaction()
        val playerData = event.player.getData()
        if (playerData.connectionAmount < 1) {
            Bukkit.broadcast(
                Component.text("Bienvenue à ").append(
                    playerNameFancy(event.player)
                ).append(
                    Component.text("!")
                )
            )
        } else {
            event.player.sendMessage(Component.text("Rebonjour ").append(playerNameFancy(event.player)))
        }
        playerData.connectionAmount += 1

        connectionWarning()
        notInProductionMessage(event.player)

        transaction.commit()
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
        player.healthScale = calculateScaledHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
        player.sendHealthUpdate()
    }

    @EventHandler
    fun onPlayerEquipArmor(event: PlayerArmorChangeEvent) {
//        server.scheduler.runTaskLater(plugin, Runnable {  }, 20L)
        updatePlayerHealthScaling(event.player)
    }

}