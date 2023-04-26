package com.cludivers.kz2wdprison.framework.beans.artifact

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

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Specifications::class)
    var consumers: Map<Specifications, ItemStack> = mapOf()

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Specifications::class)
    var producers: Map<Specifications, ItemStack> = mapOf()

    @Transient
    var flow: Float = 0f

    var activateOnInteract: Boolean = false

    // TODO : Create a converter for item stacks
    @Transient
    var itemStack: ItemStack? = null

    fun activate(session: Session, entity: org.bukkit.entity.Entity, location: Location){
        flow = producers.map { it.key.produce(session, it.value, entity, location) }.sumOf { it.toDouble() }.toFloat()
        if (consumers.isNotEmpty()){
            val flowValue = flow / consumers.size
            consumers.forEach { it.key.consume(session, it.value, entity, location, flowValue) }
        }
    }

    fun generateEditorMenu(): StoringMenu{
        val producersSlotsOffset = 0
        val producersSpecsOffset = 9
        val consumersSlotsOffset = 27
        val consumersSpecsOffset = 36
        val producersSlots = (producersSlotsOffset until producers.size + producersSlotsOffset)
        val producersSpecs = (producersSpecsOffset until producers.size + producersSpecsOffset)
        val consumersSlots = (consumersSlotsOffset until consumers.size + consumersSlotsOffset)
        val consumersSpecs = (consumersSpecsOffset until consumers.size + consumersSpecsOffset)
        val slots: Set<Int> = (producersSlots + producersSpecs + consumersSlots + consumersSpecs).toSet()
        val editor = object: StoringMenu(slots){
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, 5 * 9, Component.text("Artifact Edition"))
                producers.entries.withIndex().forEach {
                    inventory.setItem(it.index + producersSlotsOffset, it.value.value)
                    inventory.setItem(it.index + producersSpecsOffset, it.value.value) // Yeah I know, I suck because I didn't push work made at home. I'll modify it later :)
                }
                for (i in 18 until 27){
                    inventory.setItem(i, ItemStack(Material.LIGHT_GRAY_STAINED_GLASS))
                }
                consumers.entries.withIndex().forEach {
                    inventory.setItem(it.index + consumersSlotsOffset, it.value.value)
                    inventory.setItem(it.index + consumersSpecsOffset, it.value.value) // Yeah I know, I suck because I didn't push work made at home. I'll modify it later :)
                }
                return inventory
            }

            override fun close(player: Player) {
                // TODO : Register artifact change here
            }
        }

        return editor
    }
}