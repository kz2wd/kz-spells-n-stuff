package com.cludivers.kz2wdprison.modules.shards

import com.cludivers.kz2wdprison.framework.commands.ServerCommand.Companion.initializeCommands
import com.cludivers.kz2wdprison.modules.shards.listeners.ShardListener
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object ShardsDeclaration {

    fun declare(plugin: JavaPlugin, server: Server) {

        /*
        I truly wonder which one I prefer between using reflection and

         */
        initializeCommands("com.cludivers.kz2wdprison.modules.shards.commands")
        server.pluginManager.registerEvents(ShardListener(server, plugin), plugin)

    }
}