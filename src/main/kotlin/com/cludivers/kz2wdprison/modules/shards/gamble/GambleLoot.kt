package com.cludivers.kz2wdprison.modules.shards.gamble

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class GambleLoot(private val material: Material, private var _min: Int? = null, private var _max: Int? = null, private var _weight: Int? = null) {

    var weight: Int
        get() = _weight ?: DEFAULT_WEIGHT
        set(value) {
            if (_weight != null) return
            _weight = value
        }

    var min: Int
        get() = _min ?: DEFAULT_MIN
        set(value) {
            if (_min != null) return
            _min = value
        }

    var max: Int
        get() = _max ?: DEFAULT_MAX
        set(value) {
            if (_max != null) return
            _max = value
        }

    fun get(): ItemStack {
        val quantity = if (min != max) Random.nextInt(min, max) else min
        return ItemStack(material, quantity)
    }

    companion object {
        const val DEFAULT_MIN = 1
        const val DEFAULT_MAX = 100
        const val DEFAULT_WEIGHT = 100
    }
}