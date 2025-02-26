package com.cludivers.kz2wdprison.modules.mines.worldgeometry

import org.bukkit.Material
import java.util.*
import kotlin.random.Random

class BlockPattern(blocks: List<Pair<Material, Float>>) {

    private val patternMap = buildSummedNormalizedPattern(blocks)

    private fun buildSummedNormalizedPattern(blocks: List<Pair<Material, Float>>) : TreeMap<Float, Material> {
        val patternProbabilitySum = blocks.sumOf { it.second.toDouble() }.toFloat()
        var sumMap = 0f
        val probabilitiesSummed = blocks.map {
            sumMap += it.second / patternProbabilitySum
            sumMap
        }

        return TreeMap<Float, Material>(probabilitiesSummed.zip(blocks.map { it.first }).associate { it.first to it.second })
    }

    fun getMaterial(): Material{
        return patternMap.higherEntry(Random.nextFloat()).value
    }

}