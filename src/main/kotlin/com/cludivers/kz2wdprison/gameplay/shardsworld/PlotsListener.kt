package com.cludivers.kz2wdprison.gameplay.shardsworld

import io.papermc.paper.event.block.PlayerShearBlockEvent
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.*

class PlotsListener : Listener {

    private val defaultRules: PlotRules = PlotRules()

    private fun getRules(location: Location): PlotRules {
        return PlotState.getPlotState(location)?.plotRules ?: defaultRules
    }

    private fun <T> handleCancellableBlockEvent(event: T) where T : BlockEvent, T : Cancellable {
        handleWorldModification(event) { event.block.location }
    }

    private fun <T> handleWorldModification(event: T, getLocation: () -> Location) where T : Event, T : Cancellable {
        handleCancellableEvent(event) { !getRules(getLocation()).allowModifications }
    }

    private fun <T> handleCancellableEvent(
        event: T,
        getCancellingCondition: () -> Boolean
    ) where T : Event, T : Cancellable {
        if (getCancellingCondition()) {
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
        handleWorldModification(event) { event.block.location }
    }

    @EventHandler
    fun onModification(event: EntityChangeBlockEvent) {
        handleWorldModification(event) { event.block.location }
    }

    @EventHandler
    fun onMobSpawn(event: CreatureSpawnEvent) {
        handleCancellableEvent(event) { !getRules(event.location).mobSpawning }
    }

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        val foodDelta = event.foodLevel - event.entity.foodLevel
        if (foodDelta > 0) {
            return
        }
        val rules = getRules(event.entity.location)
        event.foodLevel *= rules.playerHungerPercentage / 100
    }

    @EventHandler
    fun onPlayerRegen(event: EntityRegainHealthEvent) {
        event.amount *= getRules(event.entity.location).entityRegenerationScalingPercentage / 100
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            event.damage *= getRules(event.entity.location).playerReceivedDamageScalingPercentage / 100
        } else {
            event.damage *= getRules(event.entity.location).mobReceivedDamageScalingPercentage / 100
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            event.damage *= getRules(event.entity.location).playerInflictedDamageScalingPercentage / 100
        } else {
            event.damage *= getRules(event.entity.location).mobInflictedDamageScalingPercentage / 100
        }
    }

    @EventHandler
    fun onPlayerPvp(event: EntityDamageByEntityEvent) {
        if (event.damager is Player && event.entity is Player) {
            handleCancellableEvent(event) { !getRules(event.entity.location).allowPvp }
        }
    }
}