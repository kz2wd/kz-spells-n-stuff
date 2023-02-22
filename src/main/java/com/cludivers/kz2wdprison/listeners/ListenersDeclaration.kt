package com.cludivers.kz2wdprison.listeners

import com.cludivers.kz2wdprison.menu.MenuListener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session

object ListenersDeclaration {
    fun declare(plugin: JavaPlugin, pluginManager: PluginManager, session: Session){
        val prisonListener = PrisonListener(plugin, session);

        pluginManager.registerEvents(prisonListener, plugin)
        pluginManager.registerEvents(MenuListener, plugin)

    }
}