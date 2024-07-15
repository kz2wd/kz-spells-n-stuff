package com.cludivers.kz2wdprison.listeners

import com.cludivers.kz2wdprison.menu.MenuListener
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object ListenersDeclaration {
    fun declare(plugin: JavaPlugin, server: Server) {
        server.pluginManager.registerEvents(MenuListener, plugin)
        server.pluginManager.registerEvents(ShardListener(server, plugin), plugin)
    }
}