package com.cludivers.kz2wdprison.gameplay.shardsworld

import jakarta.persistence.Embeddable
import net.kyori.adventure.text.Component
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.random.Random

@Embeddable
class PlotRules {

    // weatherType

    // breakEffect
    // placeEffect
    var allowModifications: Boolean = false
    var breakingShardRequirement: Int = 0
    var placeShardRequirement: Int = 0

    // Put all world's rules here

    var shardsLootingPercentage: Int = 100

    // playerEntityEffect

    var allowPvp: Boolean = false

    var playerInflictedDamageScalingPercentage: Int = 100
    var playerReceivedDamageScalingPercentage: Int = 100
    var playerHungerPercentage: Int = 100

    var entityRegenerationScalingPercentage: Int = 100

    // MobsEntityEffect

    var mobSpawning: Boolean = true
    var mobGriefing: Boolean = true
    var mobInflictedDamageScalingPercentage: Int = 100
    var mobReceivedDamageScalingPercentage: Int = 100

    fun showWorldRules(): Component {
        return Component.text(
            "Modifications : $allowModifications\n" +
                    "Shard looting : $shardsLootingPercentage %\n" +
                    "Pvp : $allowPvp\n" +
                    "Player inflicted damage : $playerInflictedDamageScalingPercentage %\n" +
                    "Player received damage $playerReceivedDamageScalingPercentage %\n" +
                    "Player hunger percentage $playerHungerPercentage %\n" +
                    "Entity regeneration $entityRegenerationScalingPercentage %\n" +
                    "Mob spawn : $mobSpawning\n" +
                    "Mob grief : $mobGriefing\n" +
                    "Mob inflicted damage $mobInflictedDamageScalingPercentage %\n" +
                    "Mob received damage $mobReceivedDamageScalingPercentage %\n"
        )
    }

    // fallingBehavior: FallingBehavior = FallingBehavior.None
    companion object {
        private fun generateNormalDistribution(mean: Double, stdDev: Double, random: Random): () -> Double {
            return {
                val u1 = random.nextDouble()
                val u2 = random.nextDouble()
                mean + stdDev * sqrt(-2 * ln(u1)) * cos(2 * Math.PI * u2)
            }
        }

        fun generatePseudoRandomRules(seed: Int, generalDifficultyFactor: Float): PlotRules {
            val rules = PlotRules()
            val random = Random(seed)
            val normalDistribution = generateNormalDistribution(
                100.0,
                10.0 * generalDifficultyFactor,
                random
            )

            rules.shardsLootingPercentage = normalDistribution().toInt()
            rules.playerInflictedDamageScalingPercentage = normalDistribution().toInt()
            rules.playerReceivedDamageScalingPercentage = normalDistribution().toInt()
            rules.playerHungerPercentage = normalDistribution().toInt()
            rules.entityRegenerationScalingPercentage = normalDistribution().toInt()
            rules.mobInflictedDamageScalingPercentage = normalDistribution().toInt()
            rules.mobReceivedDamageScalingPercentage = normalDistribution().toInt()

            return rules
        }
    }
}