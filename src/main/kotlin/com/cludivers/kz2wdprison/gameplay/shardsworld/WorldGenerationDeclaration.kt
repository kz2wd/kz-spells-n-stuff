package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.shardsworld.command.TravelWorldsCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.command.WorldGeneratorCommand
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object WorldGenerationDeclaration {
    fun declare(plugin: JavaPlugin, server: Server) {
        WorldGenerator.plugin = plugin


        val commandName = "shardland"
        val commandExecutor = MainCommandExecutor(
            mapOf(
                "generate" to WorldGeneratorCommand(commandName),
                "travel" to TravelWorldsCommand(commandName),
            )
        )
        plugin.getCommand(commandName)?.setExecutor(commandExecutor)
        plugin.getCommand(commandName)?.tabCompleter = commandExecutor

        server.pluginManager.registerEvents(ShardsWorldListener(), plugin)

    }
}