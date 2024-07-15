package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.artifact.ArtifactDeclaration
import com.cludivers.kz2wdprison.attributes.AttributesDeclaration
import com.cludivers.kz2wdprison.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.menu.MenuListener
import com.cludivers.kz2wdprison.mines.MinesDeclaration
import com.cludivers.kz2wdprison.namespaces.CustomNamespacesManager
import com.cludivers.kz2wdprison.nation.NationDeclaration
import com.cludivers.kz2wdprison.shardsworld.WorldGenerationDeclaration
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