package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.bukkit.Bukkit
import org.bukkit.World

@Entity
class WorldRules {
    @Id
    @GeneratedValue
    var id: Long? = null

    var linkedWorld: String? = null

    // weatherType

    // breakEffect
    // placeEffect
    var allowModifications: Boolean = false
    var breakingShardRequirement: Int = 0
    var placeShardRequirement: Int = 0

    // Put all world's rules here

    var shardsLootingPercentage: Int = 100

    // playerEntityEffect

    var playerHealthScalingPercentage: Int = 100
    var playerDamageScalingPercentage: Int = 100
    var playerHungerPercentage: Int = 100

    // MobsEntityEffect

    var mobSpawning: Boolean = true
    var mobGriefing: Boolean = true
    var mobHealthScalingPercentage: Int = 100
    var mobDamageScalingPercentage: Int = 100

    companion object {

        fun fetchAllAssignedWorldRules(): Map<World, WorldRules> {
            return PluginConfiguration.session
                .createQuery("from WorldRules where linkedWorld is not null", WorldRules::class.java)
                .list()
                .map { Bukkit.getWorld(it.linkedWorld!!) to it }
                .mapNotNull { it.first?.let { world -> Pair(world, it.second) } }
                .toMap()
        }
    }

}