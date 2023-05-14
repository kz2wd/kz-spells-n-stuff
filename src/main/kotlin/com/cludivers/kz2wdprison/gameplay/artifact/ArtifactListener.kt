package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.player.IntrinsicAttributes
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact2
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import com.cludivers.kz2wdprison.gameplay.player.getData
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

class ArtifactListener(private val session: Session): Listener {

    companion object {
        private val artifacts: MutableMap<ItemStack, Artifact2> = mutableMapOf()
        fun registerArtifact(artifact: Artifact2, itemStack: ItemStack){
            artifacts[itemStack] = artifact
        }
    }

    @EventHandler
    fun onArtifactInteracted(event: PlayerInteractEvent){
        if (event.item === null){
            return
        }
        val artifact: Artifact2? = artifacts[event.item]
        if (artifact === null){
            return
        }
        // Artifact Editor, add level condition later ?
        if (event.player.isSneaking){
            artifact.generateEditorMenu().open(event.player)
        } else {
            artifact.activate(Caster.playerToCaster(event.player), 100)
        }
    }
}