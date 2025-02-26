package com.cludivers.kz2wdprison.modules.artifact

import com.cludivers.kz2wdprison.framework.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.modules.artifact.commands.ArtifactAddCommand
import com.cludivers.kz2wdprison.modules.artifact.commands.ArtifactDemoCommand
import com.cludivers.kz2wdprison.modules.artifact.commands.ArtifactHelperCommand
import com.cludivers.kz2wdprison.modules.artifact.commands.ArtifactResourcePackCommand
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
                "enable_resourcepack" to ArtifactResourcePackCommand(
                    artifactCommandName
                ),
            )
        )

        plugin.getCommand(artifactCommandName)?.setExecutor(artifactCommandExecutor)
        plugin.getCommand(artifactCommandName)?.tabCompleter = artifactCommandExecutor

        server.pluginManager.registerEvents(com.cludivers.kz2wdprison.modules.artifact.listeners.ArtifactListener(), plugin)

        DefaultArtifacts.initDefaultArtifacts()
    }

}