package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.commands.xp.IncreaseHealthCommand
import com.cludivers.kz2wdprison.commands.xp.XpShowCommand
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.service.ServiceRegistry

class Kz2wdPrison : JavaPlugin() {
    override fun onEnable() {
        logger.info("Hello World!")

        val configuration = CustomConfiguration.getConfiguration()

        val serviceRegistry: ServiceRegistry =
            StandardServiceRegistryBuilder().applySettings(configuration.properties).build()

        val sessionFactory = configuration.buildSessionFactory(serviceRegistry)
        val session = sessionFactory.openSession()

        val prisonListener = PrisonListener(session)
        server.pluginManager.registerEvents(prisonListener, this)

        // Set up xp command

        val xpCmd = XpShowCommand(session)
        val xpCommands = mapOf<String, CommandExecutor>("health" to IncreaseHealthCommand(session, prisonListener))

        this.getCommand("xp")?.setExecutor(MainCommandExecutor(xpCommands, xpCmd))

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}