package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.commands.*
import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import org.bukkit.plugin.java.JavaPlugin

object ArtifactDeclaration {

    fun declare(plugin: JavaPlugin) {

        val artifactCommandName = "artifact"
        val artifactCommandExecutor = MainCommandExecutor(
            mapOf(
                "runes" to ArtifactHelperCommand(artifactCommandName),
                "create" to ArtifactAddCommand(artifactCommandName),
                "create_input" to ArtifactInputRuneAddCommand(artifactCommandName),
                "demo" to ArtifactDemoCommand(artifactCommandName),
                "enable_resourcepack" to ArtifactResourcePackCommand(artifactCommandName),
            )
        )

        plugin.getCommand(artifactCommandName)?.setExecutor(artifactCommandExecutor)
        plugin.getCommand(artifactCommandName)?.tabCompleter = artifactCommandExecutor

        DefaultArtifacts.initDefaultArtifacts()
    }

}