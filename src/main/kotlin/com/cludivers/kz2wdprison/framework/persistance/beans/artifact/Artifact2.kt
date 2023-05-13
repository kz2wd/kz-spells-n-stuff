package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.InputTypes
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Artifact2 {

    var itemStack: ItemStack = ItemStack(Material.AIR)
    var input: ItemStack = ItemStack(Material.AIR)
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

        ArtifactEffects.getMaterialGroup(itemStack).triggerArtifactEffect(itemStack, input)

        return 0
    }

}