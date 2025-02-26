package com.cludivers.kz2wdprison.modules.menus

import com.cludivers.kz2wdprison.modules.menus.listeners.MenuListener
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object MenuDeclaration {

    fun declare(plugin: JavaPlugin, server: Server) {
        server.pluginManager.registerEvents(MenuListener, plugin)
    }
}