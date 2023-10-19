package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.gameplay.artifact.listeners.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.menu.MenuListener
import com.cludivers.kz2wdprison.gameplay.nation.listeners.NationListener
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object ListenersDeclaration {
    fun declare(plugin: JavaPlugin, server: Server) {
        server.pluginManager.registerEvents(NationListener(), plugin)
        server.pluginManager.registerEvents(MenuListener, plugin)
        server.pluginManager.registerEvents(ArtifactListener(), plugin)
        server.pluginManager.registerEvents(ShardListener(server, plugin), plugin)
    }
}