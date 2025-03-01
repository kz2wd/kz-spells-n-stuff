package com.cludivers.kz2wdprison.modules.artifact.listeners

import com.cludivers.kz2wdprison.modules.artifact.Artifact
import com.cludivers.kz2wdprison.modules.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.modules.artifact.ArtifactExecution
import com.cludivers.kz2wdprison.modules.artifact.ArtifactTriggers
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ItemDespawnEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ArtifactListener : Listener {

    @Suppress("unused")
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
                artifact.activate(ArtifactActivator.playerToCaster(event.player), 100f, ArtifactTriggers.getClickType(event.action))
            }
            return
        }
    }

    @Suppress("unused")
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

    @Suppress("unused")
    @EventHandler
    fun onItemLinkedDestroyed(event: ItemDespawnEvent) {
        val artifact = Artifact.getArtifact(event.entity.itemStack)
        if (artifact != null) {
            Artifact.deleteArtifact(artifact)
        }
    }


    @Suppress("unused")
    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        Bukkit.broadcast(Component.text("HIT"))
        trackedProjectile[event.entity]?.invoke(
            insertBlockOrEntity(
                event.hitEntity,
                event.hitBlock
            )
        )

    }

    companion object {
        var trackedProjectile: MutableMap<Projectile, ((ArtifactExecution) -> ArtifactExecution) -> Unit> =
            emptyMap<Projectile, ((ArtifactExecution) -> ArtifactExecution) -> Unit>().toMutableMap()

        private fun insertBlockOrEntity(entity: Entity?, block: Block?): (ArtifactExecution) -> ArtifactExecution {
            return fun(input: ArtifactExecution): ArtifactExecution {
                if (entity != null) {
                    input.entities.add(entity)
                }
                if (block != null) {
                    input.locations.add(block.location)
                }
                return input
            }
        }
    }
}