package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.menu.MenuListener
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