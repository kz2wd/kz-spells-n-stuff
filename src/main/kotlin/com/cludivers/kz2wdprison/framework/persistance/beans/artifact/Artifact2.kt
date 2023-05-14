package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.InputTypes
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Artifact2 {

    companion object {
        private val defaultItemStack = ItemStack(Material.AIR)
    }

    var itemStack: ItemStack = defaultItemStack
    var input: ItemStack = defaultItemStack
    var converters: List<ItemStack> = listOf()

    fun activate(caster: Caster, inFlow: Int): Int {

        val maxDistance = 10  // Handle this later

        val input: ArtifactInput = when (InputTypes.getInputType(input)) {
            InputTypes.ENTITY_CASTER -> ArtifactInput(inFlow, entities = listOf(caster.getSelf()))
            InputTypes.ENTITY_SIGHT -> {
                val entitySight = caster.getSightEntity(maxDistance) ?: return inFlow
                ArtifactInput(inFlow, entities = listOf(entitySight))
            }
            InputTypes.ENTITY_BOUND -> return inFlow
            InputTypes.LOCATION_SIGHT -> {
                val locationSight = caster.getSightBlock(maxDistance)?.location ?: return inFlow
                ArtifactInput(inFlow, listOf(locationSight))
            }
            InputTypes.NONE -> return inFlow
        }

        converters.map { Converters.getConverter(itemStack) }.forEach { it.convertInput(input) }

        ArtifactEffects.getMaterialGroup(itemStack).triggerArtifactEffect(itemStack, input, caster.getSelf() as Player)

        return 0
    }
    fun generateEditorMenu(): StoringMenu {
        val inventorySize = 5 * 9
        val itemStackSlot = 4 * 9 - 1
        val inputSlot = 9
        val convertersSlots = (inputSlot + 1  until itemStackSlot - 1 )

        val fillingSlots = (0 until inventorySize) - convertersSlots - itemStackSlot - inputSlot
        val slots: Set<Int> = (convertersSlots + itemStackSlot + inputSlot).toSet()
        val editor = object: StoringMenu(slots, true){
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Artifact Edition"))
                inventory.setItem(itemStackSlot, itemStack)
                inventory.setItem(inputSlot, input)

                converters.zip(convertersSlots).forEach {
                    inventory.setItem(it.second, it.first)
                }

                val fillingItem =
                    Utils.buildItemStack(Component.text(""), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 684867154) // Just put a unique flag here
                fillingSlots.forEach { inventory.setItem(it, fillingItem) }

                return inventory
            }

            override fun close(player: Player) {
                input = player.openInventory.topInventory.getItem(inputSlot) ?: defaultItemStack
                itemStack = player.openInventory.topInventory.getItem(itemStackSlot) ?: defaultItemStack
                converters = convertersSlots.map { player.openInventory.topInventory.getItem(it) ?: defaultItemStack }.filter { it != defaultItemStack }
            }
        }

        return editor
    }


}