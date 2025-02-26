package com.cludivers.kz2wdprison.modules.shardsworld.rules

import com.cludivers.kz2wdprison.modules.nation.beans.FactionRuleKey
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.random.Random

@Embeddable
class PlotRules {

    @ElementCollection
    @CollectionTable(name = "faction", joinColumns = [JoinColumn(name = "faction")])
    @MapKeyClass(FactionRuleKey::class)
    var factionRulesPercentage: MutableMap<FactionRuleKey, Float> = mutableMapOf()

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    var rulesBoolean: MutableMap<RulesBoolean, Boolean> = mutableMapOf()

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    var rulesPercentage: MutableMap<RulesPercentage, Float> = mutableMapOf()

    fun showWorldRules(): Component {
        return Component.text(
            factionRulesPercentage.entries.joinToString("\n") { (key, percentage) ->
                "${key.factionRule} [${key.factionsName}]: $percentage%"
            }
                    +
            rulesPercentage.entries.joinToString("\n") { (rule, percentage) ->
                "$rule: $percentage%"
            }
                    +
            rulesBoolean.entries.joinToString("\n") { (rule, bool) ->
                "$rule: $bool"
            },
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

            com.cludivers.kz2wdprison.modules.shardsworld.rules.FactionsRulesPercentage.values().forEach { rule ->
                FactionsName.values().forEach { faction ->
                    rules.factionRulesPercentage[FactionRuleKey(rule, faction)] = normalDistribution().toFloat()
                }
            }

            RulesPercentage.values().forEach { rule ->
                rules.rulesPercentage[rule] = normalDistribution().toFloat()
            }

            RulesBoolean.values().forEach { rule ->
                rules.rulesBoolean[rule] = random.nextBoolean()
            }

            return rules
        }
    }
}