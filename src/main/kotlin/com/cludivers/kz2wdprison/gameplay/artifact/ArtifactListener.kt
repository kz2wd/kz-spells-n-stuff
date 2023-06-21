package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
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
                artifact.activate(Caster.playerToCaster(event.player, session), 100f)
            }
            return
        }
        val artifactComplexRune: ArtifactComplexRune? = ArtifactComplexRune.artifactComplexRunes[event.item]
        if (artifactComplexRune !== null) {
            artifactComplexRune.runeType.generateEditorMenu(session, artifactComplexRune).open(event.player)
        }

    }
}