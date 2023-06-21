package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactAddCommand
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactHelperCommand
import com.cludivers.kz2wdprison.gameplay.commands.artifact.ArtifactInputRuneAddCommand
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session

object ArtifactDeclaration {

    fun declare(plugin: JavaPlugin, session: Session) {
        ArtifactComplexRune.initPersistentArtifactComplexRune(session)
        Artifact.initPersistentArtifacts(session)


        val artifactCommandName = "artifact"
        val artifactCommandExecutor = MainCommandExecutor(
            mapOf(
                "runes" to ArtifactHelperCommand(artifactCommandName),
                "create" to ArtifactAddCommand(artifactCommandName, session),
                "create_input" to ArtifactInputRuneAddCommand(artifactCommandName, session),
            )
        )

        plugin.getCommand(artifactCommandName)?.setExecutor(artifactCommandExecutor)
        plugin.getCommand(artifactCommandName)?.tabCompleter = artifactCommandExecutor
    }

}