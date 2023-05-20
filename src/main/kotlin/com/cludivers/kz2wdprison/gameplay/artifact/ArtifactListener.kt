package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

class ArtifactListener(private val session: Session): Listener {

    companion object {
        private val artifacts: MutableMap<ItemStack, Artifact> = mutableMapOf()
        fun registerArtifact(artifact: Artifact, itemStack: ItemStack) {
            artifacts[itemStack] = artifact
        }

        fun isItemStackLinked(item: ItemStack): Boolean {
            return artifacts.containsKey(item)
        }

        fun initPersistentArtifacts(session: Session) {
            val artifacts = session.createQuery("from Artifact A", Artifact::class.java).list()
            artifacts.filter { it.linkedItemStack != null }.forEach { registerArtifact(it, it.linkedItemStack!!) }
        }
    }

    @EventHandler
    fun onArtifactInteracted(event: PlayerInteractEvent){
        if (event.item === null){
            return
        }
        val artifact: Artifact? = artifacts[event.item]
        if (artifact === null){
            return
        }
        // Artifact Editor, add level condition later ?
        if (event.player.isSneaking){
            artifact.generateEditorMenu(session).open(event.player)
        } else {
            artifact.activate(Caster.playerToCaster(event.player), 100)
        }
    }
}