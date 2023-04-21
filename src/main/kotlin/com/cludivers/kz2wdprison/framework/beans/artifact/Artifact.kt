package com.cludivers.kz2wdprison.framework.beans.artifact

import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
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
}