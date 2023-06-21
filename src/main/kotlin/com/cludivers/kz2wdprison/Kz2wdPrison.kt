package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.configuration.HibernateConfigurationHandler
import com.cludivers.kz2wdprison.framework.persistance.beans.player.AttributeItem
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactDeclaration
import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.intrinsic.*
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


        AttributeItem.initPersistentArtifactComplexRune(session)
        val attributeItemCommandName = "intrinsic"
        val attributeItemCommandExecutor = MainCommandExecutor(
            mapOf(
                "create" to AttributeItemAdd(attributeItemCommandName, session),
                "help" to IntrinsicHelpCommand(attributeItemCommandName, session),
                "fill" to AttributeItemFill(attributeItemCommandName, session),
                "unfill" to AttributeItemUnfill(attributeItemCommandName, session),
                "equip" to AttributeItemEquip(attributeItemCommandName, session),
                "unequip" to AttributeItemUnequip(attributeItemCommandName, session),
                "info" to AttributeItemInfo(attributeItemCommandName, session),
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