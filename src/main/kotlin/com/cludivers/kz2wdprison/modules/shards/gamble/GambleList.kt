package com.cludivers.kz2wdprison.modules.shards.gamble

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class GambleList(val name: Component, val pullCost: Int, itemsAndWeights: List<GambleLoot>) {

    private val cumulativeWeights: MutableList<Pair<GambleLoot, Int>> = mutableListOf()
    private val weightSum = itemsAndWeights.sumOf { it.weight }

    init {
        var weightSum = 0
        itemsAndWeights.forEach { loot ->
            weightSum += loot.weight
            cumulativeWeights.add(loot to weightSum)
        }
    }

    fun pull(): ItemStack {
        val rng = Random.nextInt(weightSum)
        val index = cumulativeWeights.binarySearch {  it.second - rng }.let { if (it < 0) -it - 1 else it }
        return cumulativeWeights[index].first.get()
    }

    fun getPullErrorMessage(): Component {
        return Component.text("You don't have enough shards for ", NamedTextColor.RED)
            .append(this.name)
            .append(Component.text(" (", NamedTextColor.RED))
            .append(Component.text(this.pullCost.toString(), Style.style(TextDecoration.BOLD)))
            .append(Component.text(")", NamedTextColor.RED))
    }

    companion object {
        val BASIC_LOOTBOX = GambleList(Component.text("Basic lootbox").decorate(TextDecoration.ITALIC),
            100, listOf(
                GambleLoot(Material.IRON_INGOT, 5, 10),
                GambleLoot(Material.COAL, 5, 10),
                GambleLoot(Material.OAK_LOG, 5, 10, 1),
                GambleLoot(Material.SPRUCE_LOG, 5, 10, 1),
                GambleLoot(Material.BIRCH_LOG, 5, 10, 1),
                GambleLoot(Material.ACACIA_LOG, 5, 10, 1),
                GambleLoot(Material.MANGROVE_LOG, 5, 10, 1),
                GambleLoot(Material.DARK_OAK_LOG, 5, 10, 1),
                GambleLoot(Material.CHERRY_LOG, 5, 10, 1),
                GambleLoot(Material.DIAMOND, 1, 1, 1),
                ))
    }

}
