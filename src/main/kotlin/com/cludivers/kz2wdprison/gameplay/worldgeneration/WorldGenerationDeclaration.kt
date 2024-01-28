package com.cludivers.kz2wdprison.gameplay.worldgeneration

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.worldgeneration.worldgenerator.WorldGenerator
import com.cludivers.kz2wdprison.gameplay.worldgeneration.worldgenerator.command.TravelWorldsCommand
import com.cludivers.kz2wdprison.gameplay.worldgeneration.worldgenerator.command.WorldGeneratorCommand
import org.bukkit.plugin.java.JavaPlugin

object WorldGenerationDeclaration {
    fun declare(plugin: JavaPlugin) {
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

    }
}