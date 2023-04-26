package com.cludivers.kz2wdprison.framework.beans.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hibernate.Session
import kotlin.jvm.Transient

@Entity
class Artifact {
    @Id
    @GeneratedValue
    var id: Long? = null

    var maxConsumers: Int = 1
    @ElementCollection(targetClass = Specifications::class)
    var consumers: List<Pair<Specifications, ItemStack>> = listOf() // Should be list of object packing spec, itemstack, isModifiable, bla bla bla


    var maxProducers: Int = 1
    @ElementCollection(targetClass = Specifications::class)
    var producers: List<Pair<Specifications, ItemStack>> = listOf()

    @Transient
    var flow: Float = 0f

    var activateOnInteract: Boolean = false

    // TODO : Create a converter for item stacks
    @Transient
    var itemStack: ItemStack? = null

    var minLevelToEdit: Int = 0

    fun activate(session: Session, entity: org.bukkit.entity.Entity, location: Location){
        flow = producers.map { it.first.produce(session, it.second, entity, location) }.sumOf { it.toDouble() }.toFloat()
        if (consumers.isNotEmpty()){
            val flowValue = flow / consumers.size
            consumers.forEach { it.first.consume(session, it.second, entity, location, flowValue) }
        }
    }

    fun generateEditorMenu(): StoringMenu{
        val producersSlotsOffset = 0
        val producersSpecsOffset = 9
        val consumersSlotsOffset = 27
        val consumersSpecsOffset = 36
        val producersSlots = (producersSlotsOffset until maxProducers + producersSlotsOffset)
        val producersSpecs = (producersSpecsOffset until maxProducers + producersSpecsOffset)
        val consumersSlots = (consumersSlotsOffset until maxConsumers + consumersSlotsOffset)
        val consumersSpecs = (consumersSpecsOffset until maxConsumers + consumersSpecsOffset)
        val slots: Set<Int> = (producersSlots + producersSpecs + consumersSlots + consumersSpecs).toSet()
        val editor = object: StoringMenu(slots){
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, 5 * 9, Component.text("Artifact Edition"))
                producers.withIndex().forEach {
                    inventory.setItem(it.index + producersSlotsOffset, it.value.first.customItemStack.itemStack)
                    inventory.setItem(it.index + producersSpecsOffset, it.value.second)
                }
                for (i in 18 until 27){
                    inventory.setItem(i, ItemStack(Material.LIGHT_GRAY_STAINED_GLASS))
                }
                consumers.withIndex().forEach {
                    inventory.setItem(it.index + consumersSlotsOffset, it.value.first.customItemStack.itemStack)
                    inventory.setItem(it.index + consumersSpecsOffset, it.value.second)
                }
                return inventory
            }

            override fun close(player: Player) {
            }
        }

        return editor
    }
}