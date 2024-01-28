package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.commands.ArtifactAddCommand
import com.cludivers.kz2wdprison.gameplay.artifact.commands.ArtifactDemoCommand
import com.cludivers.kz2wdprison.gameplay.artifact.commands.ArtifactHelperCommand
import com.cludivers.kz2wdprison.gameplay.artifact.commands.ArtifactResourcePackCommand
import com.cludivers.kz2wdprison.gameplay.artifact.listeners.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
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
                "enable_resourcepack" to ArtifactResourcePackCommand(artifactCommandName),
            )
        )

        plugin.getCommand(artifactCommandName)?.setExecutor(artifactCommandExecutor)
        plugin.getCommand(artifactCommandName)?.tabCompleter = artifactCommandExecutor

        server.pluginManager.registerEvents(ArtifactListener(), plugin)

        DefaultArtifacts.initDefaultArtifacts()
    }

}