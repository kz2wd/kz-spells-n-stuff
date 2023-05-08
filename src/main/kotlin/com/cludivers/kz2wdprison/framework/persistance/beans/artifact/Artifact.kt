package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.converters.ItemStackConverter
import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hibernate.Session
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import kotlin.jvm.Transient

@Entity
class Artifact {
    @Id
    @GeneratedValue
    var id: Long? = null

    var maxConsumers: Int = 1
    @ElementCollection
    var consumers: List<Pair<ItemStack, ItemStack>> = listOf()

    var maxProducers: Int = 1
    @ElementCollection
    var producers: List<Pair<ItemStack, ItemStack>> = listOf()

    @Transient
    var flow: Float = 0f

    var activateOnInteract: Boolean = false

    var cooldown: Duration = Duration.ZERO
    var lastUsage: Instant? = null

    @Convert(converter = ItemStackConverter::class)
    var itemStack: ItemStack? = null

    var minLevelToEdit: Int = 0

    fun activate(session: Session, entity: org.bukkit.entity.Entity, location: Location){
        if (lastUsage != null && Duration.between(lastUsage, Instant.now()) < cooldown){
            return
        }
        lastUsage = Instant.now()
        flow = producers.map { ProductionTypes.itemStackToProducerType(it.second).produce(session, it.first, entity, location) }.sumOf { it.toDouble() }.toFloat()
        if (consumers.isNotEmpty()){
            val flowValue = flow / consumers.size
            consumers.forEach { ConsumptionTypes.itemStackToConsumerType(it.second).consume(session, it.first, entity, location, flowValue) }
        }
    }

    fun generateEditorMenu(): StoringMenu{
        val inventorySize = 5 * 9
        val producersSlotsOffset = 0
        val producersSpecsOffset = 9
        val consumersSlotsOffset = 27
        val consumersSpecsOffset = 36
        val producersSlots = (producersSlotsOffset until maxProducers + producersSlotsOffset)
        val producersSpecs = (producersSpecsOffset until maxProducers + producersSpecsOffset)
        val consumersSlots = (consumersSlotsOffset until maxConsumers + consumersSlotsOffset)
        val consumersSpecs = (consumersSpecsOffset until maxConsumers + consumersSpecsOffset)
        val fillingSlots = (0 until inventorySize) - producersSlots - producersSpecs - consumersSlots - consumersSpecs
        val slots: Set<Int> = (producersSlots + producersSpecs + consumersSlots + consumersSpecs).toSet()
        val editor = object: StoringMenu(slots, true){
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Artifact Edition"))
                producers.withIndex().forEach {
                    inventory.setItem(it.index + producersSlotsOffset, it.value.first)
                    inventory.setItem(it.index + producersSpecsOffset, it.value.second)
                }
                val fillingItem =
                    Utils.buildItemStack(Component.text(""), Material.LIGHT_GRAY_STAINED_GLASS_PANE, 684867154) // Just put a unique flag here
                fillingSlots.forEach { inventory.setItem(it, fillingItem) }
                consumers.withIndex().forEach {
                    inventory.setItem(it.index + consumersSlotsOffset, it.value.first)
                    inventory.setItem(it.index + consumersSpecsOffset, it.value.second)
                }
                return inventory
            }

            override fun close(player: Player) {
                consumers = consumersSlots.zip(consumersSpecs).map {
                    Pair(player.openInventory.topInventory.getItem(it.first) ?: ItemStack(Material.AIR),
                        player.openInventory.topInventory.getItem(it.second)?: ItemStack(Material.AIR)
                    ) }
                producers = producersSlots.zip(producersSpecs).map {
                    Pair(player.openInventory.topInventory.getItem(it.first) ?: ItemStack(Material.AIR),
                        player.openInventory.topInventory.getItem(it.second)?: ItemStack(Material.AIR)) }
            }
        }

        return editor
    }
}