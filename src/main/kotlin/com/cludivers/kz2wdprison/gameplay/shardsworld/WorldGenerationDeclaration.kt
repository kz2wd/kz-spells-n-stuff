package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.shardsworld.command.PlotGeneratorCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.command.PlotInfoCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.command.TravelPlotsCommand
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object WorldGenerationDeclaration {
    fun declare(plugin: JavaPlugin, server: Server) {
        WorldGenerator.plugin = plugin


        val commandName = "shardland"
        val commandExecutor = MainCommandExecutor(
            mapOf(
                "generate" to PlotGeneratorCommand(commandName),
                "travel" to TravelPlotsCommand(commandName),
                "info" to PlotInfoCommand(commandName)
            )
        )
        plugin.getCommand(commandName)?.setExecutor(commandExecutor)
        plugin.getCommand(commandName)?.tabCompleter = commandExecutor

        server.pluginManager.registerEvents(PlotsListener(), plugin)

    }
}