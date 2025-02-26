package com.cludivers.kz2wdprison.modules.shards

import com.cludivers.kz2wdprison.framework.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.modules.shards.commands.CheckShardsAmount
import com.cludivers.kz2wdprison.modules.shards.listeners.ShardListener
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object ShardsDeclaration {

    fun declare(plugin: JavaPlugin, server: Server) {
        val shardCommandName = "shards"
        val shardCommandExecutor = MainCommandExecutor(
            mapOf(
                "amount" to CheckShardsAmount(shardCommandName),
            )
        )

        plugin.getCommand(shardCommandName)?.setExecutor(shardCommandExecutor)
        plugin.getCommand(shardCommandName)?.tabCompleter = shardCommandExecutor

        server.pluginManager.registerEvents(ShardListener(server, plugin), plugin)

    }
}