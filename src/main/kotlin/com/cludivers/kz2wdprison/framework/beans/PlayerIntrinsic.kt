package com.cludivers.kz2wdprison.framework.beans

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.hibernate.Session

@Embeddable
class PlayerIntrinsic {

    @ElementCollection
    var attributes: MutableMap<IntrinsicAttributes, Int> = IntrinsicAttributes.values().associateWith { 1 }.toMutableMap()

    fun increaseAttribute(session: Session, attribute: IntrinsicAttributes){
        Bukkit.broadcast(Component.text("${attribute.name}: ${attributes[attribute]}"))

        session.beginTransaction()
        attributes[attribute] = attributes[attribute]!! + 1
        session.transaction.commit()
    }

    fun decreaseAttribute(session: Session, attribute: IntrinsicAttributes){
        Bukkit.broadcast(Component.text("${attribute.name}: ${attributes[attribute]}"))
        if (attributes[attribute] !! <= 1){
            return
        }
        session.beginTransaction()
        attributes[attribute] = attributes[attribute]!! - 1
        session.transaction.commit()
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