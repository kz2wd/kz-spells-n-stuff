package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.converters.Converters
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.ArtifactEffectInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInputInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.BasicInputRunes
import com.cludivers.kz2wdprison.framework.persistance.converters.ItemStackConverter
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

@Entity
class ArtifactComplexRune : ArtifactInputInterface, ArtifactEffectInterface {

    companion object {
        val artifactComplexRunes: MutableMap<ItemStack, ArtifactComplexRune> = mutableMapOf()

        fun registerArtifactComplexRune(rune: ArtifactComplexRune, itemStack: ItemStack) {
            artifactComplexRunes[itemStack] = rune
        }

        fun isItemStackLinked(item: ItemStack): Boolean {
            return artifactComplexRunes.containsKey(item)
        }

        fun initPersistentArtifactComplexRune(session: Session) {
            val artifactComplexInputs =
                session.createQuery("from ArtifactComplexRune A", ArtifactComplexRune::class.java).list()
            artifactComplexInputs.filter { it.linkedItemStack != null }
                .forEach { registerArtifactComplexRune(it, it.linkedItemStack!!) }
        }

    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    @Convert(converter = ItemStackConverter::class)
    var mainItemStack: ItemStack = Artifact.defaultItemStack

    @ElementCollection(targetClass = ItemStack::class)
    @Convert(converter = ItemStackConverter::class)
    var extraItemStacks: List<ItemStack> = listOf()

    var runeType: ArtifactRuneTypes = ArtifactRuneTypes.NONE

    fun generateEditorMenu(session: Session): StoringMenu {
        val inventorySize = 5 * 9
        val mainSlot = 9
        val extraItemStackSlots = (mainSlot + 1 until inventorySize - 9)

        val fillingSlots = (0 until inventorySize) - extraItemStackSlots - mainSlot
        val slots: Set<Int> = (extraItemStackSlots + mainSlot).toSet()
        val editor = object : StoringMenu(slots, true) {
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Rune Crafting"))
                inventory.setItem(mainSlot, mainItemStack)

                extraItemStacks.zip(extraItemStackSlots).forEach {
                    inventory.setItem(it.second, it.first)
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
                mainItemStack = player.openInventory.topInventory.getItem(mainSlot) ?: Artifact.defaultItemStack
                extraItemStacks =
                    extraItemStackSlots.map {
                        player.openInventory.topInventory.getItem(it) ?: Artifact.defaultItemStack
                    }
                        .filter { it != Artifact.defaultItemStack }
                session.transaction.commit()
            }
        }

        return editor
    }

    override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
        TODO("Not yet implemented")
    }

    override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
        // Only search in basic runes to prevent infinite recursion leading to stack overflow
        val input: ArtifactInput =
            BasicInputRunes.getInputRune(mainItemStack)?.getArtifactInput(inputRune, caster, inFlow)
                ?: return ArtifactInput(0)

        extraItemStacks.map { Converters.getConverter(it) }.forEach { it.convertInput(input) }

        return input
    }
}

