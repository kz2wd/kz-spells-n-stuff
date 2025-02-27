package com.cludivers.kz2wdprison.modules.nation.listeners

import com.cludivers.kz2wdprison.modules.nation.NationCoreEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityTargetEvent

class NationListener: Listener {

    companion object {
        val cores: MutableMap<Entity, NationCoreEntity> = mutableMapOf()
    }

    @EventHandler
    fun onCoreAttack(event: EntityDamageByEntityEvent) {
        val nationCore = cores[event.entity] ?: return
        nationCore.damage(event.damage)
        event.isCancelled = true
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity
        if (entity is Zombie) {
            val target = event.target
            if (target == null) {
                try {
                    entity.target = cores.keys.iterator().next() as LivingEntity
                } catch (_: Exception) {}

            }
        }
    }
}