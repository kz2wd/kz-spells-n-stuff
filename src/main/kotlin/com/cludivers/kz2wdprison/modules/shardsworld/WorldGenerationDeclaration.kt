package com.cludivers.kz2wdprison.modules.shardsworld

import com.cludivers.kz2wdprison.framework.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.modules.shardsworld.command.*
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
                "regen" to RegenerateCurrentPlotCommand(commandName)
            )
        )
        plugin.getCommand(commandName)?.setExecutor(commandExecutor)
        plugin.getCommand(commandName)?.tabCompleter = commandExecutor

        server.pluginManager.registerEvents(PlotsListener(), plugin)

    }
}