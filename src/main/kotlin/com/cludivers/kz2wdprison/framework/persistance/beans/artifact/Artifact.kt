package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.InputTypes
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
import java.time.Duration
import java.time.Instant

@Entity
class Artifact {

    companion object {
        private val defaultItemStack = ItemStack(Material.AIR)
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    @Convert(converter = ItemStackConverter::class)
    var effect: ItemStack = defaultItemStack

    @Convert(converter = ItemStackConverter::class)
    var input: ItemStack = defaultItemStack

    @ElementCollection(targetClass = ItemStack::class)
    @Convert(converter = ItemStackConverter::class)
    var converters: List<ItemStack> = listOf()


    private var cooldown: Duration = Duration.ZERO
    private var lastUsage: Instant? = null

    fun activate(caster: Caster, inFlow: Int): Int {

        if (lastUsage != null && Duration.between(lastUsage, Instant.now()) < cooldown) {
            return inFlow
        }
        lastUsage = Instant.now()

        val maxDistance = 10  // Handle this later

        // Keep this here and not in a function in InputTypes enum to use access to special attributes like maxDistance
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
            InputTypes.EMPTY_LOCATION_SIGHT -> {
                val locationSight = caster.getSightAirBlock(maxDistance)?.location ?: return inFlow
                ArtifactInput(inFlow, listOf(locationSight))
            }
            InputTypes.NONE -> return inFlow
            InputTypes.LOCATION_FORWARD_EYE_CASTER -> {
                var forwardEyeLocation = (caster.getSelf() as Player).eyeLocation
                forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)
                ArtifactInput(inFlow, listOf(forwardEyeLocation))
            }
        }
        converters.map { Converters.getConverter(it) }.forEach { it.convertInput(input) }

        ArtifactEffects.getMaterialGroup(effect).triggerArtifactEffect(effect, input, caster.getSelf() as Player)

        return 0
    }
    fun generateEditorMenu(session: Session): StoringMenu {
        val inventorySize = 5 * 9
        val itemStackSlot = 4 * 9 - 1
        val inputSlot = 9
        val convertersSlots = (inputSlot + 1  until itemStackSlot - 1 )

        val fillingSlots = (0 until inventorySize) - convertersSlots - itemStackSlot - inputSlot
        val slots: Set<Int> = (convertersSlots + itemStackSlot + inputSlot).toSet()
        val editor = object: StoringMenu(slots, true){
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Artifact Edition"))
                inventory.setItem(itemStackSlot, effect)
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
                session.beginTransaction()
                input = player.openInventory.topInventory.getItem(inputSlot) ?: defaultItemStack
                effect = player.openInventory.topInventory.getItem(itemStackSlot) ?: defaultItemStack
                converters = convertersSlots.map { player.openInventory.topInventory.getItem(it) ?: defaultItemStack }
                    .filter { it != defaultItemStack }
                session.transaction.commit()
            }
        }

        return editor
    }


}