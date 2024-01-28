package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactDeclaration
import com.cludivers.kz2wdprison.gameplay.attributes.AttributesDeclaration
import com.cludivers.kz2wdprison.gameplay.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.gameplay.menu.MenuListener
import com.cludivers.kz2wdprison.gameplay.mines.MinesDeclaration
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespacesManager
import com.cludivers.kz2wdprison.gameplay.nation.NationDeclaration
import com.cludivers.kz2wdprison.gameplay.shardsworld.WorldGenerationDeclaration
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {


    override fun onEnable() {
        PluginConfiguration.loadConfigurationAndDatabase(config)

        CustomNamespacesManager.initAllNamespacedKeys(this)

        ListenersDeclaration.declare(this, server)
        MinesDeclaration.declare(this)
        NationDeclaration.declare(this, server)
        ArtifactDeclaration.declare(this, server)
        AttributesDeclaration.declare(this)
        WorldGenerationDeclaration.declare(this, server)

        server.pluginManager.registerEvents(MenuListener, this)

    }

    override fun onDisable() {
        PluginConfiguration.closeConnections()
    }

}