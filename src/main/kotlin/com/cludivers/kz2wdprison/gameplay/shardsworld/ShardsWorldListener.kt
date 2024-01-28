package com.cludivers.kz2wdprison.gameplay.shardsworld

import io.papermc.paper.event.block.PlayerShearBlockEvent
import org.bukkit.World
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class ShardsWorldListener : Listener {

    private val worldsRules: MutableMap<World, WorldRules> = WorldRules.fetchAllAssignedWorldRules().toMutableMap()
    private val defaultRules: WorldRules = WorldRules()

    private fun getRules(world: World): WorldRules {
        return worldsRules[world] ?: defaultRules
    }

    private fun <T> handleCancellableBlockEvent(event: T) where T : BlockEvent, T : Cancellable {
        handleCancellableEvent(event) { event.block.world }
    }

    private fun <T> handleCancellableEvent(event: T, getWorld: () -> World) where T : Event, T : Cancellable {
        val rules = getRules(getWorld())
        if (!rules.allowModifications) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockEvent(event: BlockBreakEvent) {
        handleCancellableBlockEvent(event)
    }

    @EventHandler
    fun onBlockEvent(event: BlockPlaceEvent) {
        handleCancellableBlockEvent(event)
    }

    @EventHandler
    fun onBlockEvent(event: BlockGrowEvent) {
        handleCancellableBlockEvent(event)
    }

    @EventHandler
    fun onBlockEvent(event: BlockFormEvent) {
        handleCancellableBlockEvent(event)
    }

    @EventHandler
    fun onBlockShear(event: PlayerShearBlockEvent) {
        handleCancellableEvent(event) { event.block.world }
    }

    @EventHandler
    fun onMobSpawn(event: CreatureSpawnEvent) {
        val rules = getRules(event.location.world)
        if (!rules.mobSpawning) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        val foodDelta = event.foodLevel - event.entity.foodLevel
        if (foodDelta > 0) {
            return
        }
        val rules = getRules(event.entity.world)
        event.foodLevel *= rules.playerHungerPercentage / 100
    }

}