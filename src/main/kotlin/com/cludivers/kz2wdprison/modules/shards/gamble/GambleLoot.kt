package com.cludivers.kz2wdprison.modules.shards.gamble

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class GambleLoot(private val material: Material, private val min: Int = 1, private val max: Int = 64, val weight: Int = 10) {
    fun get(): ItemStack {
        val quantity = Random.nextInt(min, max)
        return ItemStack(material, quantity)
    }
}