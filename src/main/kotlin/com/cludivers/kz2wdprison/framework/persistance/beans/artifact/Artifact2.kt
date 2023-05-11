package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class Artifact2 {

    private var itemStack: ItemStack = ItemStack(Material.AIR)
    private var input: ItemStack = ItemStack(Material.AIR)
    private var converters: List<ItemStack> = listOf()

    fun <T> activate(caster: Caster, inFlow: Int): Int {
        val maxDistance = 5  // Handle this later
        val input = when (InputTypes.getInputType(input)) {
            InputTypes.ENTITY_CASTER -> caster.getSelf()
            InputTypes.ENTITY_SIGHT -> caster.getSightEntity(maxDistance)
            InputTypes.ENTITY_BOUND -> null
            InputTypes.LOCATION_SIGHT -> caster.getSightBlock(maxDistance)?.location
            InputTypes.NONE -> null
        }



        return 0
    }

    private fun resolveConverters(){
        converters.map { Converters.getConverter(it) }

    }

    private fun getConverter(converter: Converters): (Any) -> Any {
        return when (converter) {
            Converters.ENTITIES_POSITION -> { entities: List<Entity> -> entities.map { it.location } }
            Converters.SHIFT_POSITIONS -> { locations: List<Location>, shift: Vector -> locations.map { it.add(shift) }}
            Converters.POSITIONS_AROUND -> { locations: List<Location>, radius: Int -> locations.map { it }}  // I'll do it later
            Converters.ENTITIES_AROUND -> { locations: List<Location>, radius: Double -> locations.map { it.getNearbyEntities(radius) }.flatten()}
            Converters.NONE -> { it: Any -> it }
        }
    }

}