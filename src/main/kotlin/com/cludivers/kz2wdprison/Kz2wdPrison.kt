package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.configuration.HibernateConfigurationHandler
import com.cludivers.kz2wdprison.gameplay.attributes.PlayerAttributesDeclaration
import com.cludivers.kz2wdprison.gameplay.event.BonusXpEvent
import com.cludivers.kz2wdprison.gameplay.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.gameplay.nation.NationDeclaration
import com.cludivers.kz2wdprison.gameplay.world.mines.MinesDeclaration
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {
    override fun onEnable() {
        val sessionFactory = HibernateConfigurationHandler.loadHibernateConfiguration(config)
        val session = sessionFactory.openSession()

        PlayerAttributesDeclaration.declare(this, session)
        ListenersDeclaration.declare(this,  server.pluginManager, session)
        MinesDeclaration.declare(this)
        NationDeclaration.declare(this, session)

        BonusXpEvent.start(this)

        // val currentEventCmd = CurrentEventCommand()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}