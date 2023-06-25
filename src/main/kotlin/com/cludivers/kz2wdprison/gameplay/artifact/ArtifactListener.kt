package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactTriggers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

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
                artifact.activate(ArtifactActivator.playerToCaster(event.player), 100f, ArtifactTriggers.CLICK)
            }
            return
        }
        val artifactComplexRune: ArtifactComplexRune? = ArtifactComplexRune.getComplexRune(event.item!!)
        if (artifactComplexRune !== null) {
            artifactComplexRune.runeType.generateEditorMenu(artifactComplexRune).open(event.player)
        }
    }

    @EventHandler
    fun triggerAttackArtifact(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val attacker = (event.damager as Player)
            val artifact: Artifact? = Artifact.getArtifact(attacker.inventory.itemInMainHand)
            if (artifact !== null) {
                artifact.activate(
                    ArtifactActivator.playerToCaster(attacker, attacker = event.damager, event.entity),
                    100f,
                    ArtifactTriggers.ATTACKING
                )
            }
        }
        if (event.entity is Player) {
            val attacked = (event.entity as Player)

            val itemsToCheck: List<ItemStack> =
                attacked.inventory.armorContents!!.filterNotNull() + attacked.inventory.itemInMainHand
            itemsToCheck.forEach {
                val artifact: Artifact? = Artifact.getArtifact(it)
                if (artifact !== null) {
                    artifact.activate(
                        ArtifactActivator.playerToCaster(
                            attacked,
                            attacker = event.damager,
                            event.entity
                        ), 100f, ArtifactTriggers.ATTACKED
                    )
                }
            }

        }
    }
}