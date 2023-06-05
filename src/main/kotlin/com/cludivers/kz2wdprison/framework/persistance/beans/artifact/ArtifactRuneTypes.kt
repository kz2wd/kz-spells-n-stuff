package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.converters.ArtifactConverterInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.converters.BasicArtifactConverters
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
import org.hibernate.Session

enum class ArtifactRuneTypes : ArtifactInputInterface, ArtifactEffectInterface, ArtifactConverterInterface {
    INPUT_RUNE {
        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            val basicRune = BasicInputRunes.getInputRune(inputRune)
            if (basicRune != null) {
                return basicRune.getArtifactInput(inputRune, caster, inFlow)
            }

            // If it is not a basic input type :
            val complexRune = ArtifactComplexRune.artifactComplexRunes[inputRune]
            if (complexRune == null || complexRune.runeType != INPUT_RUNE) {
                return super.getArtifactInput(inputRune, caster, inFlow)
            }
            // If a complexRune of type INPUT was found, then resolve its input
            return complexRune.getArtifactInput(inputRune, caster, inFlow)
        }
    },
    EFFECT_RUNE {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            BasicArtifactEffects.getEffectType(itemStack).triggerArtifactEffect(itemStack, input, player)
        }
    },
    CONVERTER_RUNE {
        override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
            val basicConverter = BasicArtifactConverters.getConverter(itemStack)
            if (basicConverter != null) {
                basicConverter.convertInput(itemStack, input)
                return
            }

            // If it is not a basic converter type :
            val complexRune = ArtifactComplexRune.artifactComplexRunes[itemStack]
            if (complexRune == null || complexRune.runeType != CONVERTER_RUNE) {
                return
            }
            // If a complexRune of type INPUT was found, then resolve its input
            complexRune.convertInput(itemStack, input)
        }

        override fun generateEditorMenu(session: Session, complexRune: ArtifactComplexRune): StoringMenu {
            val inventorySize = 5 * 9
            val itemStackSlots =
                (1 until 5).map { (1 * it until 5 * it).toList() }.reduce { acc, ints -> acc + ints } - 22
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
                    session.beginTransaction()
                    complexRune.stockedItemStack = itemStackSlots.associateWith {
                        player.openInventory.topInventory.getItem(it)
                    }.filter { it.value != null } as Map<Int, ItemStack>
                    session.transaction.commit()
                }
            }

            return editor
        }
    },
    NONE;

    open fun generateEditorMenu(session: Session, complexRune: ArtifactComplexRune): StoringMenu {
        val inventorySize = 5 * 9
        val itemStackSlots = (1 until inventorySize - 9)

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
                session.beginTransaction()
                complexRune.stockedItemStack = itemStackSlots.associateWith {
                    player.openInventory.topInventory.getItem(it)
                }.filter { it.value != null } as Map<Int, ItemStack>
                session.transaction.commit()
            }
        }

        return editor
    }

    override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
        // Do nothing
    }

    override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
        return ArtifactInput(0)
    }

    override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
        // Do nothing
    }
}