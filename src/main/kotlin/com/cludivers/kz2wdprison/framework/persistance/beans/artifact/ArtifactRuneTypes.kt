package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.ArtifactEffectInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.BasicArtifactEffects
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInputInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.BasicInputRunes
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

enum class ArtifactRuneTypes : ArtifactInputInterface, ArtifactEffectInterface {
    GENERIC_INPUT_RUNE {
        override fun enrichArtifactInput(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            if (inputRune.itemMeta == null) {
                return
            }

            val basicRune = BasicInputRunes.getInputRune(inputRune)
            if (basicRune != null) {
                basicRune.enrichArtifactInput(inputRune, artifactActivator, input, inputsTrace)
                return
            }

            // If it is not a basic input type :s
            val complexRune = ArtifactComplexRune.getComplexRune(inputRune)
            if (complexRune == null || complexRune.runeType != GENERIC_INPUT_RUNE) {
                return
            }
            // If a complexRune of type INPUT was found, then resolve its input
            return complexRune.enrichArtifactInput(inputRune, artifactActivator, input, inputsTrace)
        }
    },
    GENERIC_EFFECT_RUNE {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            BasicArtifactEffects.getEffectType(itemStack).triggerArtifactEffect(itemStack, input, player)
        }
    },

    BOUND_ENTITY_RUNE {
        override fun enrichArtifactInput(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            // TODO
            input.entities
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
                HibernateSession.session.beginTransaction()
                complexRune.stockedItemStack = itemStackSlots.associateWith {
                    player.openInventory.topInventory.getItem(it)
                }.filter { it.value != null } as Map<Int, ItemStack>
                HibernateSession.session.transaction.commit()
            }
        }

        return editor
    }

    override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
        // Do nothing
    }

    override fun enrichArtifactInput(
        inputRune: ItemStack,
        artifactActivator: ArtifactActivator,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>
    ) {
        // Do nothing
    }


}