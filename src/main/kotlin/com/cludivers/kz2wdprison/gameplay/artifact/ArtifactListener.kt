package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class ArtifactListener : Listener {

    @EventHandler
    fun onArtifactInteracted(event: PlayerInteractEvent) {
        if (event.item === null) {
            return
        }
        val artifact: Artifact? = Artifact.getArtifact(event.item!!)
        if (artifact !== null) {
            // Artifact Editor, add level condition later ?
            if (event.player.isSneaking) {
                artifact.generateEditorMenu().open(event.player)
            } else {
                artifact.activate(Caster.playerToCaster(event.player), 100f)
            }
            return
        }
        val artifactComplexRune: ArtifactComplexRune? = ArtifactComplexRune.getComplexRune(event.item!!)
        if (artifactComplexRune !== null) {
            artifactComplexRune.runeType.generateEditorMenu(artifactComplexRune).open(event.player)
        }

    }
}