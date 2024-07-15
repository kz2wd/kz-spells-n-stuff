package com.cludivers.kz2wdprison.shardsworld

import com.cludivers.kz2wdprison.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.shardsworld.command.*
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object WorldGenerationDeclaration {
    fun declare(plugin: JavaPlugin, server: Server) {
        WorldGenerator.plugin = plugin


        val commandName = "plot"
        val commandExecutor = MainCommandExecutor(
            mapOf(
                "generate" to PlotGeneratorCommand(commandName),
                "travel" to TravelPlotsCommand(commandName),
                "info" to PlotInfoCommand(commandName),
                "test" to TestCommand(commandName),
                "regen" to com.cludivers.kz2wdprison.shardsworld.command.RegenerateCurrentPlotCommand(commandName)
            )
        )
        plugin.getCommand(commandName)?.setExecutor(commandExecutor)
        plugin.getCommand(commandName)?.tabCompleter = commandExecutor

        server.pluginManager.registerEvents(PlotsListener(), plugin)

    }
}