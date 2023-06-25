package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.menu.MenuListener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

object ListenersDeclaration {
    fun declare(plugin: JavaPlugin, pluginManager: PluginManager) {
        pluginManager.registerEvents(NationListener(), plugin)
        pluginManager.registerEvents(MenuListener, plugin)
        pluginManager.registerEvents(ArtifactListener(), plugin)
        pluginManager.registerEvents(ShardListener(), plugin)
    }
}