package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.commands.xp.IncreaseAttributeCommand
import com.cludivers.kz2wdprison.commands.xp.XpShowCommand
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.service.ServiceRegistry


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {
    override fun onEnable() {
        logger.info("Hello World!")

        val configuration = CustomConfiguration.getConfiguration()

        val serviceRegistry: ServiceRegistry =
            StandardServiceRegistryBuilder().applySettings(configuration.properties).build()

        val sessionFactory = configuration.buildSessionFactory(serviceRegistry)
        val session = sessionFactory.openSession()


        // Set up xp command

        val xpCommandName = "xp"

        // Player Attributes
        val successText = Component.text("${ChatColor.GREEN}Amélioration de")


        val healthAttribute = PlayerAttribute(
            "PV",
            -1,
            "health",
            xpCommandName,
            {p: PlayerBean -> p.healthLevel},
            {p: PlayerBean -> p.healthLevel += 1},
            {p: PlayerBean -> if (p.healthLevel < 10) { 1 } else { 2 }},
            {n: Int -> "${PrisonListener.getHealth(n).toInt()} PV"},
            successText.append(Component.text(" vos PV !")))


        val pickaxesName = listOf("bois", "pierre", "fer", "diamant", "netherite")
        val pickaxeAttribute = PlayerAttribute(
            "Pioche",
            4,
            "pickaxe",
            xpCommandName,
            {p: PlayerBean -> p.pickaxeLevel},
            {p: PlayerBean -> p.pickaxeLevel += 1},
            {p: PlayerBean -> p.pickaxeLevel + 1},
            {n: Int -> "Pioche en ${pickaxesName[n]}"},
            successText.append(Component.text(" votre ${ChatColor.BOLD}pioche !")))

        val miningSpeedAttribute = PlayerAttribute(
            "Minage",
            5,
            "mining",
            xpCommandName,
            {p: PlayerBean -> p.miningLevel},
            {p: PlayerBean -> p.miningLevel += 1},
            {p: PlayerBean -> 1},
            {n: Int -> "Efficacité $n" },
            successText.append(Component.text(" votre vitesse de minage !")))


        val allAttributes = listOf(pickaxeAttribute, miningSpeedAttribute, healthAttribute )

        val xpCmd = XpShowCommand(session)
        val xpCommands = allAttributes.associate { it.increaseCommandCallName to IncreaseAttributeCommand(session, it) }

        this.getCommand(xpCommandName)?.setExecutor(MainCommandExecutor(xpCommands, xpCmd))

        val prisonListener = PrisonListener(session, allAttributes)
        server.pluginManager.registerEvents(prisonListener, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}