package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.menu.MenuListener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session

object ListenersDeclaration {
    fun declare(plugin: JavaPlugin, pluginManager: PluginManager, session: Session){
        val prisonListener = PrisonListener(plugin, session);

        pluginManager.registerEvents(NationListener(session), plugin)
        pluginManager.registerEvents(prisonListener, plugin)
        pluginManager.registerEvents(MenuListener, plugin)
        pluginManager.registerEvents(ArtifactListener(session), plugin)
    }
}