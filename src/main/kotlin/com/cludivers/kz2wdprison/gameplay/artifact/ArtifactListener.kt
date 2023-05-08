package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.player.IntrinsicAttributes
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.gameplay.player.getData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

class ArtifactListener(private val session: Session): Listener {

    companion object {
        private val artifacts: MutableMap<ItemStack, Artifact> = mutableMapOf()
        fun registerArtifact(artifact: Artifact){
            if (artifact.itemStack === null){
                return
            }
            artifacts[artifact.itemStack!!] = artifact
        }
    }

    @EventHandler
    fun onArtifactInteracted(event: PlayerInteractEvent){
        if (event.item === null){
            return
        }
        val artifact: Artifact? = artifacts[event.item]
        if (artifact === null || !artifact.activateOnInteract){
            return
        }
        // Artifact Editor, add level condition later ?
        if (event.player.isSneaking
            && artifact.minLevelToEdit <= event.player.getData(session).intrinsic
                .attributes[IntrinsicAttributes.ARTIFACT_ARCHITECT]!!){
            artifact.generateEditorMenu().open(event.player)
        } else {
            artifact.activate(session, event.player, event.player.eyeLocation)
        }
    }

}