package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.configuration.HibernateConfigurationHandler
import com.cludivers.kz2wdprison.framework.persistance.beans.player.AttributeItem
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactDeclaration
import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.attribute.AttributeItemAdd
import com.cludivers.kz2wdprison.gameplay.event.BonusXpEvent
import com.cludivers.kz2wdprison.gameplay.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.gameplay.nation.NationDeclaration
import com.cludivers.kz2wdprison.gameplay.world.mines.MinesDeclaration
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session
import org.hibernate.SessionFactory


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {

    lateinit var sessionFactory: SessionFactory
    lateinit var session: Session

    override fun onEnable() {
        sessionFactory = HibernateConfigurationHandler.loadHibernateConfiguration(config)
        session = sessionFactory.openSession()

        ListenersDeclaration.declare(this, server.pluginManager, session)
        MinesDeclaration.declare(this)
        NationDeclaration.declare(this, session)
        ArtifactDeclaration.declare(this, session)

        BonusXpEvent.start(this)

        AttributeItem.initPersistentArtifactComplexRune(session)
        val attributeItemCommandName = "item"
        val attributeItemCommandExecutor = MainCommandExecutor(
            mapOf(
                "create" to AttributeItemAdd(attributeItemCommandName, session),
            )
        )

        this.getCommand(attributeItemCommandName)?.setExecutor(attributeItemCommandExecutor)
        this.getCommand(attributeItemCommandName)?.tabCompleter = attributeItemCommandExecutor

    }

    override fun onDisable() {
        session.close()
        sessionFactory.close()
    }

}