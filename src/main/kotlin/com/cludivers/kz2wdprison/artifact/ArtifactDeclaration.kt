package com.cludivers.kz2wdprison.artifact

import com.cludivers.kz2wdprison.artifact.commands.ArtifactAddCommand
import com.cludivers.kz2wdprison.artifact.commands.ArtifactDemoCommand
import com.cludivers.kz2wdprison.artifact.commands.ArtifactHelperCommand
import com.cludivers.kz2wdprison.artifact.listeners.ArtifactListener
import com.cludivers.kz2wdprison.commands.MainCommandExecutor
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object ArtifactDeclaration {

    fun declare(plugin: JavaPlugin, server: Server) {

        val artifactCommandName = "artifact"
        val artifactCommandExecutor = MainCommandExecutor(
            mapOf(
                "runes" to ArtifactHelperCommand(artifactCommandName),
                "create" to ArtifactAddCommand(artifactCommandName),
                "demo" to ArtifactDemoCommand(artifactCommandName),
                "enable_resourcepack" to com.cludivers.kz2wdprison.artifact.commands.ArtifactResourcePackCommand(
                    artifactCommandName
                ),
            )
        )

        plugin.getCommand(artifactCommandName)?.setExecutor(artifactCommandExecutor)
        plugin.getCommand(artifactCommandName)?.tabCompleter = artifactCommandExecutor

        server.pluginManager.registerEvents(ArtifactListener(), plugin)

        DefaultArtifacts.initDefaultArtifacts()
    }

}