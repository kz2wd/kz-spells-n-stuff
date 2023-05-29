package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInputRune
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.hibernate.Session

class ArtifactListener(private val session: Session): Listener {


    @EventHandler
    fun onArtifactInteracted(event: PlayerInteractEvent) {
        if (event.item === null) {
            return
        }
        val artifact: Artifact? = Artifact.artifacts[event.item]
        if (artifact !== null) {
            // Artifact Editor, add level condition later ?
            if (event.player.isSneaking) {
                artifact.generateEditorMenu(session).open(event.player)
            } else {
                artifact.activate(Caster.playerToCaster(event.player), 100)
            }
            return
        }
        val artifactInputRune: ArtifactInputRune? = ArtifactInputRune.artifactInputRunes[event.item]
        if (artifactInputRune !== null) {
            artifactInputRune.generateEditorMenu(session).open(event.player)
        }

    }
}