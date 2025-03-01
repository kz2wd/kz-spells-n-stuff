package com.cludivers.kz2wdprison.modules.shards.gamble

import com.cludivers.kz2wdprison.framework.utils.Utils.buildItemStack
import dev.triumphteam.gui.guis.GuiItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class LootBox(val name: Component, val material: Material, val pullCost: Int, private val itemsAndWeights: List<GambleLoot>, private val defaultMin: Int? = null, private val defaultMax: Int? = null, private val defaultWeight: Int? = null) {

    private val cumulativeWeights: MutableList<Pair<GambleLoot, Int>> = mutableListOf()
    private val finalWeightSum: Int

    init {
        if (defaultMin != null) {
            itemsAndWeights.forEach { it.min = defaultMin }
        }
        if (defaultMax != null) {
            itemsAndWeights.forEach { it.max = defaultMax }
        }
        if (defaultWeight != null) {
            itemsAndWeights.forEach { it.weight = defaultWeight }
        }
        var weightSum = 0
        itemsAndWeights.forEach { loot ->
            weightSum += loot.weight
            cumulativeWeights.add(loot to weightSum)
        }

        finalWeightSum = itemsAndWeights.sumOf { it.weight }
    }

    fun pull(): ItemStack {
        val rng = Random.nextInt(finalWeightSum)
        val index = cumulativeWeights.binarySearch {  it.second - rng }.let { if (it < 0) -it - 1 else it }
        return cumulativeWeights[index].first.get()
    }

    fun getDropPercents(): List<Pair<GambleLoot, Float>> {
        return itemsAndWeights.associateWith { it.weight.toFloat() / finalWeightSum * 100 }.toList()
    }

    fun getPullErrorMessage(): Component {
        return Component.text("You don't have enough shards for ", NamedTextColor.RED)
            .append(this.name)
            .append(Component.text(" (", NamedTextColor.RED))
            .append(Component.text(this.pullCost.toString(), Style.style(TextDecoration.BOLD)))
            .append(Component.text(")", NamedTextColor.RED))
    }

    fun getGuiPreview(): GuiItem {
        val lore: MutableList<Component> = getDropPercents().map { Component.text("${it.first.material.name}: ${"%.2f".format(it.second)}%") }.toMutableList()
        val previewInfo = name.append(Component.text(" | $pullCost Shards").decoration(TextDecoration.BOLD, true))
        val preview = buildItemStack(previewInfo, material, lore = lore)

        return GuiItem(preview)
    }

    companion object {
        val BASIC_LOOTBOX = LootBox(Component.text("Basic lootbox").decorate(TextDecoration.ITALIC), Material.WOODEN_SWORD,
            100, listOf(
                GambleLoot(Material.IRON_INGOT),
                GambleLoot(Material.COAL),
                GambleLoot(Material.OAK_LOG, _weight = 1),
                GambleLoot(Material.SPRUCE_LOG, _weight = 1),
                GambleLoot(Material.BIRCH_LOG, _weight = 1),
                GambleLoot(Material.ACACIA_LOG, _weight = 1),
                GambleLoot(Material.MANGROVE_LOG, _weight = 1),
                GambleLoot(Material.DARK_OAK_LOG, _weight = 1),
                GambleLoot(Material.CHERRY_LOG, _weight = 1),
                GambleLoot(Material.DIAMOND, 1, 1, 1),
        ), 5, 10, 10)

        val LOG_LOOTBOX = LootBox(Component.text("Log lootbox").decorate(TextDecoration.ITALIC), Material.OAK_LOG,
            100, listOf(
                GambleLoot(Material.OAK_LOG),
                GambleLoot(Material.SPRUCE_LOG),
                GambleLoot(Material.BIRCH_LOG),
                GambleLoot(Material.ACACIA_LOG),
                GambleLoot(Material.MANGROVE_LOG),
                GambleLoot(Material.DARK_OAK_LOG),
                GambleLoot(Material.CHERRY_LOG),
            ), 15, 50)

        val FOOD_LOOTBOX = LootBox(Component.text("Food Lootbox").decorate(TextDecoration.ITALIC), Material.BREAD,
            100, listOf(
                GambleLoot(Material.APPLE),
                GambleLoot(Material.BREAD),
                GambleLoot(Material.BAKED_POTATO),
                GambleLoot(Material.COOKED_COD),
                GambleLoot(Material.COOKED_BEEF),
                GambleLoot(Material.COOKED_RABBIT),
                GambleLoot(Material.COOKED_PORKCHOP),
                GambleLoot(Material.COOKED_SALMON),
                GambleLoot(Material.COOKED_CHICKEN),
                GambleLoot(Material.SUSPICIOUS_STEW, 1, 1),
                GambleLoot(Material.GOLDEN_APPLE),
                GambleLoot(Material.GOLDEN_CARROT, _weight = 100),
                GambleLoot(Material.GOLDEN_CARROT, 64, 64, 1),
                GambleLoot(Material.ENCHANTED_GOLDEN_APPLE, 1, 1, 10),
                GambleLoot(Material.ENCHANTED_GOLDEN_APPLE, 1, 10, 1),
            ),  3, 15,500)

        val ORE_LOOTBOX = LootBox(Component.text("Ore Lootbox").decorate(TextDecoration.ITALIC), Material.RAW_IRON,
            300, listOf(
                GambleLoot(Material.COBBLESTONE),
                GambleLoot(Material.RAW_IRON),
                GambleLoot(Material.RAW_IRON_BLOCK, _weight = 50),
                GambleLoot(Material.COAL),
                GambleLoot(Material.COAL_BLOCK, _weight = 50),
                GambleLoot(Material.RAW_COPPER),
                GambleLoot(Material.RAW_COPPER_BLOCK, _weight = 50),
                GambleLoot(Material.LAPIS_LAZULI, 5, 32, _weight = 50),
                GambleLoot(Material.LAPIS_BLOCK, 1, 32, _weight = 30),
                GambleLoot(Material.REDSTONE, 5, 32, _weight = 60),
                GambleLoot(Material.REDSTONE_BLOCK, 1, 20, _weight = 30),
                GambleLoot(Material.RAW_GOLD, 10, 20, _weight = 50),
                GambleLoot(Material.RAW_GOLD_BLOCK, 10, 20, _weight = 20),
                GambleLoot(Material.DIAMOND, 3, 10, _weight = 20),
                GambleLoot(Material.DIAMOND_BLOCK, 1, 10, _weight = 10),
                GambleLoot(Material.EMERALD, 1, 10, _weight = 15),
                GambleLoot(Material.EMERALD_BLOCK, 1, 10, _weight = 15),
                GambleLoot(Material.DIAMOND_BLOCK, 32, 64, _weight = 3),
                GambleLoot(Material.NETHERITE_SCRAP, 2, 16, _weight = 10),
                GambleLoot(Material.NETHERITE_INGOT, 1, 4, _weight = 5),
                GambleLoot(Material.NETHERITE_BLOCK, 1, 3, _weight = 1),

            ),  12, 32,100)


    val ALL_LOOTBOXES = listOf(BASIC_LOOTBOX, LOG_LOOTBOX, FOOD_LOOTBOX, ORE_LOOTBOX)

    }

}
