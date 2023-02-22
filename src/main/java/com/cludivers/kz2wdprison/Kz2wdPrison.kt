package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.attributes.PlayerAttributesDeclaration
import com.cludivers.kz2wdprison.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.world.mines.MinesDeclaration
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {
    override fun onEnable() {
        val sessionFactory = HibernateConfigurationHandler.loadHibernateConfiguration(config)
        val session = sessionFactory.openSession()

        PlayerAttributesDeclaration.declare(this, session)
        ListenersDeclaration.declare(this,  server.pluginManager, session)
        MinesDeclaration.declare(this)

        BonusXpEvent.start(this)

        // val currentEventCmd = CurrentEventCommand()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}