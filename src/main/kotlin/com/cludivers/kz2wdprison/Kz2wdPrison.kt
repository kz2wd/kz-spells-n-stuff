package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.modules.attributes.AttributesDeclaration
import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.menus.MenuDeclaration
import com.cludivers.kz2wdprison.modules.mines.MinesDeclaration
import com.cludivers.kz2wdprison.framework.namespaces.CustomNamespacesManager
import com.cludivers.kz2wdprison.modules.artifact.ArtifactDeclaration
import com.cludivers.kz2wdprison.modules.nation.NationDeclaration
import com.cludivers.kz2wdprison.modules.shards.ShardsDeclaration
import com.cludivers.kz2wdprison.modules.shardsworld.WorldGenerationDeclaration
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {

    override fun onEnable() {
        plugin = this
        MAIN_WORLD = Bukkit.getWorld("world")!!
        PluginConfiguration.loadConfigurationAndDatabase(config)

        CustomNamespacesManager.initAllNamespacedKeys(this)

        ShardsDeclaration.declare(this, server)
        MenuDeclaration.declare(this, server)
        MinesDeclaration.declare(this)
        NationDeclaration.declare(this, server)
        ArtifactDeclaration.declare(this, server)
        AttributesDeclaration.declare(this)
        WorldGenerationDeclaration.declare(this, server)

    }

    override fun onDisable() {
        PluginConfiguration.closeConnections()
    }

    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var MAIN_WORLD: World
    }

}