package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactAddCommand
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactDemoCommand
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactHelperCommand
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactInputRuneAddCommand
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
            )
        )

        plugin.getCommand(artifactCommandName)?.setExecutor(artifactCommandExecutor)
        plugin.getCommand(artifactCommandName)?.tabCompleter = artifactCommandExecutor

        DefaultArtifacts.initDefaultArtifacts()
    }

}