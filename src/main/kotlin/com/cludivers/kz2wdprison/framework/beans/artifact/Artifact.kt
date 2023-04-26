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
        val slots: Set<Int> =
            ((0 until producers.size) + (9 until producers.size + 9) + (27 until consumers.size + 27) + (36 until consumers.size + 36)).toSet()
        Bukkit.broadcast(Component.text(slots.joinToString { it.toString() }))
        val editor = object: StoringMenu(slots){
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, 5 * 9, Component.text("Artifact Edition"))
                producers.entries.withIndex().forEach {
                    inventory.setItem(it.index, it.value.value)
                    inventory.setItem(it.index + 9, it.value.value) // Yeah I know, I suck because I didn't push work made at home. I'll modify it later :)
                }
                for (i in 18 until 27){
                    inventory.setItem(i, ItemStack(Material.LIGHT_GRAY_STAINED_GLASS))
                }
                consumers.entries.withIndex().forEach {
                    inventory.setItem(it.index + 27, it.value.value)
                    inventory.setItem(it.index + 36, it.value.value) // Yeah I know, I suck because I didn't push work made at home. I'll modify it later :)
                }
                return inventory
            }

            override fun close(player: Player) {
                // Register artifact change here
                Bukkit.broadcast(Component.text("Updating artifact"))
            }
        }

        return editor
    }
}