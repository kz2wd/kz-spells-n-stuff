package com.cludivers.kz2wdprison.gameplay.worldgeneration.worldgenerator

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class WorldRules {
    @Id
    @GeneratedValue
    var id: Long? = null

    var loreName: String = "Unknown World"

    // weatherType

    // breakEffect
    // placeEffect

    var breakingShardRequirement: Int = 0
    var placeShardRequirement: Int = 0

    var mobGriefing: Boolean = true

    // Put all world's rules here

    var shardsLootingPercentage: Int = 100

    // playerEntityEffect

    var playerHealthScalingPercentage: Int = 100
    var playerDamageScalingPercentage: Int = 100
    var playerFoodConsumptionPercentage: Int = 100

    // MobsEntityEffect
    var mobHealthScalingPercentage: Int = 100
    var mobDamageScalingPercentage: Int = 100


}