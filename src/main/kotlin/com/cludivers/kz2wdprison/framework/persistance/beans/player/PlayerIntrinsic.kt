package com.cludivers.kz2wdprison.framework.persistance.beans.player

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hibernate.Session

@Embeddable
class PlayerIntrinsic {

    @ElementCollection
    var attributes: MutableMap<IntrinsicAttributes, Int> =
        IntrinsicAttributes.values().associateWith { it.baseValue }.toMutableMap()

    @ElementCollection
    var skills: MutableMap<PlayerSkills, Int> = mutableMapOf()

    fun increaseAttribute(session: Session, attribute: IntrinsicAttributes) {
        Bukkit.broadcast(Component.text("${attribute.name}: ${attributes[attribute]}"))

        session.beginTransaction()
        attributes[attribute] = attributes[attribute]!! + 1
        session.transaction.commit()
    }

    fun decreaseAttribute(session: Session, attribute: IntrinsicAttributes) {
        Bukkit.broadcast(Component.text("${attribute.name}: ${attributes[attribute]}"))
        if (attributes[attribute]!! <= 1) {
            return
        }
        session.beginTransaction()
        attributes[attribute] = attributes[attribute]!! - 1
        session.transaction.commit()
    }

    fun updatePlayer(player: Player) {
        IntrinsicAttributes.values().forEach {
            updatePlayer(player, it)
        }
    }

    fun updatePlayer(player: Player, attribute: IntrinsicAttributes) {
        player.getAttribute(attribute.relatedAttribute)?.baseValue =
            attribute.intrinsicToGenericValue(attributes[attribute]!!)
    }

}