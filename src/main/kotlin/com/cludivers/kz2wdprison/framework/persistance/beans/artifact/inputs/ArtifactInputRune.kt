package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Converters
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
class ArtifactInputRune {

    companion object {

        val artifactInputRunes: MutableMap<ItemStack, ArtifactInputRune> = mutableMapOf()
        fun registerArtifact(artifactInputRune: ArtifactInputRune, itemStack: ItemStack) {
            artifactInputRunes[itemStack] = artifactInputRune
        }

        fun isItemStackLinked(item: ItemStack): Boolean {
            return artifactInputRunes.containsKey(item)
        }

        fun initPersistentArtifactInputRune(session: Session) {
            val artifactInputRunes =
                session.createQuery("from ArtifactInputRune A", ArtifactInputRune::class.java).list()
            artifactInputRunes.filter { it.linkedItemStack != null }
                .forEach { registerArtifact(it, it.linkedItemStack!!) }
        }
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    @Convert(converter = ItemStackConverter::class)
    var inputType: ItemStack = Artifact.defaultItemStack

    @ElementCollection(targetClass = ItemStack::class)
    @Convert(converter = ItemStackConverter::class)
    var converters: List<ItemStack> = listOf()


    fun getArtifactInput(caster: Caster, inFlow: Int): ArtifactInput {
        val maxDistance = 10  // Handle this later

        // Keep this here and not in a function in InputTypes enum to use access to special attributes like maxDistance
        val input: ArtifactInput = when (InputTypes.getInputType(inputType)) {
            InputTypes.ENTITY_CASTER -> ArtifactInput(inFlow, entities = listOf(caster.getSelf()))
            InputTypes.ENTITY_SIGHT -> {
                val entitySight = caster.getSightEntity(maxDistance) ?: return ArtifactInput(inFlow)
                ArtifactInput(inFlow, entities = listOf(entitySight))
            }

            InputTypes.ENTITY_BOUND -> return ArtifactInput(inFlow)
            InputTypes.LOCATION_SIGHT -> {
                val locationSight = caster.getSightBlock(maxDistance)?.location ?: return ArtifactInput(inFlow)
                ArtifactInput(inFlow, listOf(locationSight))
            }

            InputTypes.EMPTY_LOCATION_SIGHT -> {
                val locationSight = caster.getSightAirBlock(maxDistance)?.location ?: return ArtifactInput(inFlow)
                ArtifactInput(inFlow, listOf(locationSight))
            }

            InputTypes.NONE -> return ArtifactInput(inFlow)
            InputTypes.LOCATION_FORWARD_EYE_CASTER -> {
                var forwardEyeLocation = (caster.getSelf() as Player).eyeLocation
                forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)
                ArtifactInput(inFlow, listOf(forwardEyeLocation))
            }
        }

        converters.map { Converters.getConverter(it) }.forEach { it.convertInput(input) }

        return input
    }

    fun generateEditorMenu(session: Session): StoringMenu {
        val inventorySize = 5 * 9
        val itemStackSlot = 4 * 9 - 1
        val inputSlot = 9
        val convertersSlots = (inputSlot + 1 until itemStackSlot - 1)

        val fillingSlots = (0 until inventorySize) - convertersSlots - itemStackSlot - inputSlot
        val slots: Set<Int> = (convertersSlots + itemStackSlot + inputSlot).toSet()
        val editor = object : StoringMenu(slots, true) {
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Artifact Input Edition"))
                inventory.setItem(inputSlot, inputType)

                converters.zip(convertersSlots).forEach {
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
                inputType = player.openInventory.topInventory.getItem(inputSlot) ?: Artifact.defaultItemStack
                converters =
                    convertersSlots.map { player.openInventory.topInventory.getItem(it) ?: Artifact.defaultItemStack }
                        .filter { it != Artifact.defaultItemStack }
                session.transaction.commit()
            }
        }

        return editor
    }

}