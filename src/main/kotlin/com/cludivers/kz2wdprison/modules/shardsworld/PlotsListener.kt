package com.cludivers.kz2wdprison.modules.shardsworld

import com.cludivers.kz2wdprison.framework.utils.MaterialCosts.getMaterialCost
import com.cludivers.kz2wdprison.modules.nation.beans.FactionRuleKey
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.modules.player.giveShards
import com.cludivers.kz2wdprison.modules.shardsworld.rules.*
import io.papermc.paper.event.block.PlayerShearBlockEvent
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.*

class PlotsListener : Listener {

    private val defaultRules: PlotRules = PlotRules.generatePseudoRandomRules(0, 1.0f)

    private fun getRules(location: Location): PlotRules {
        return PlotState.getPlotState(location)?.plotRules ?: defaultRules
    }


    private fun getFactionRulePercentage(entity: Entity, rule: FactionsRulesPercentage): Float {
        // Resolve entity faction
        return getRules(entity.location).factionRulesPercentage[FactionRuleKey(rule, FactionsName.TRIMARIS)]!! / 100
    }

    private fun getRulePercentage(location: Location, rule: RulesPercentage): Float {
        // Resolve entity faction
        return getRules(location).rulesPercentage[rule]!! / 100
    }

    private fun <T> handleCancellableBlockEvent(event: T) where T : BlockEvent, T : Cancellable {
        handleWorldModification(event) { event.block.location }
    }

    private fun <T> handleWorldModification(event: T, getLocation: () -> Location) where T : Event, T : Cancellable {
        // if ALLOW_MODIFICATION == True -> do not cancel, so invert it
        handleCancellableEvent(event) { !getRules(getLocation()).rulesBoolean[RulesBoolean.ALLOW_MODIFICATION]!! }
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
        if (event.isCancelled) return
        val blockValue = getMaterialCost(event.block.type)
        val shardRequest = event.player.getData().shardsEfficiencyFactor * blockValue * getRulePercentage(
            event.block.location,
            RulesPercentage.SHARDS_LOOTING
        )
        val plotState = PlotState.getPlotState(event.block.location) ?: return
        val shardsTaken = plotState.takeShards(shardRequest)
        event.player.giveShards(shardsTaken)

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
        handleCancellableEvent(event) { !getRules(event.location).rulesBoolean[RulesBoolean.MOB_SPAWNING]!! }
    }

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        val foodDelta = event.foodLevel - event.entity.foodLevel
        if (foodDelta > 0) {
            return
        }
        event.foodLevel *= getFactionRulePercentage(event.entity, FactionsRulesPercentage.HUNGER_PERCENTAGE).toInt()
    }

    @EventHandler
    fun onPlayerRegen(event: EntityRegainHealthEvent) {
        event.amount *= getFactionRulePercentage(event.entity, FactionsRulesPercentage.REGENERATION_SCALING)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        event.damage *= getFactionRulePercentage(event.entity, FactionsRulesPercentage.RECEIVED_DAMAGE_SCALING)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            event.damage *= getFactionRulePercentage(event.entity, FactionsRulesPercentage.INFLICTED_DAMAGE_SCALING)
        } else {
            event.damage *= getFactionRulePercentage(event.entity, FactionsRulesPercentage.RECEIVED_DAMAGE_SCALING)
        }
    }

    @EventHandler
    fun onPlayerPvp(event: EntityDamageByEntityEvent) {
        if (event.damager is Player && event.entity is Player) {
            handleCancellableEvent(event) { !getRules(event.entity.location).rulesBoolean[RulesBoolean.ALLOW_PVP]!! }
        }
    }
}