package com.cludivers.kz2wdprison.framework.beans

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

@Embeddable
class PlayerIntrinsic {

    var attributesMaxValues: Int =  5

    @ElementCollection
    var attributes: MutableMap<IntrinsicAttributes, Int> = IntrinsicAttributes.values().associateWith { 1 }.toMutableMap()

    fun increaseAttribute(attribute: IntrinsicAttributes){
        if (attributes[attribute]!! >= attributesMaxValues){
            return
        }
        attributes[attribute] = attributes[attribute]!! + 1
    }

    fun decreaseAttribute(attribute: IntrinsicAttributes){
        if (attributes[attribute] !! <= 1){
            return
        }
        attributes[attribute] = attributes[attribute]!! - 1
    }

    private fun updatePlayer(player: Player, attribute: IntrinsicAttributes){
        when (attribute){
            IntrinsicAttributes.VIGOR -> {
                // 20 is default max health value, add scaling relative to shards later
                val playerHealth = attributes[IntrinsicAttributes.VIGOR]!! / attributes.values.sum() * 20.0
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = playerHealth
                player.health = playerHealth
            }
        }

    }

}