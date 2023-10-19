package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput
import com.cludivers.kz2wdprison.gameplay.artifact.beans.ArtifactComplexRune
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

enum class ArtifactRuneTypes : ArtifactRuneInterface {
    GENERIC_ARTIFACT_RUNE {
        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            if (inputRune.itemMeta == null) {
                return
            }

            val basicRune = ArtifactRunes.getArtifactRune(inputRune)
            if (basicRune != null) {
                basicRune.processArtifactActivation(inputRune, artifactActivator, input, inputsTrace, player)
                return
            }

            // If it is not a basic input type :s
            val complexRune = ArtifactComplexRune.getComplexRune(inputRune)
            if (complexRune == null || complexRune.runeType != GENERIC_ARTIFACT_RUNE) {
                return
            }
            // If a complexRune of type INPUT was found, then resolve its input
            return complexRune.processArtifactActivation(inputRune, artifactActivator, input, inputsTrace, player)
        }
    },

    BOUND_ENTITY_RUNE {
        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            // TODO

        }
    },
    NONE;

    open fun generateEditorMenu(complexRune: ArtifactComplexRune): StoringMenu {
        val inventorySize = 5 * 9
        val itemStackSlots = (1 until inventorySize - 9)
        // Keep it there, I'll need it later
//        val itemStackSlots =
//            (1 until 5).map { (1 * it until 5 * it).toList() }.reduce { acc, ints -> acc + ints } - 22

        val fillingSlots = (0 until inventorySize) - itemStackSlots
        val slots: Set<Int> = (itemStackSlots).toSet()
        val editor = object : StoringMenu(slots, true) {
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Rune Crafting"))

                complexRune.stockedItemStack.forEach {
                    inventory.setItem(it.key, it.value)
                }

                val fillingItem =
                    Utils.buildItemStack(
                        Component.text(""),
                        Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                        684867154
                    ) // Just put a unique flag here
                fillingSlots.forEach { inventory.setItem(it, fillingItem) }

                return inventory
            }

            override fun close(player: Player) {
                PluginConfiguration.session.beginTransaction()
                complexRune.stockedItemStack = itemStackSlots.associateWith {
                    player.openInventory.topInventory.getItem(it)
                }.filter { it.value != null } as Map<Int, ItemStack>
                PluginConfiguration.session.transaction.commit()
            }
        }

        return editor
    }

    override fun processArtifactActivation(
        inputRune: ItemStack,
        artifactActivator: ArtifactActivator,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>,
        player: Player?
    ) {
        // Do nothing
    }


}