package com.cludivers.kz2wdprison.framework.beans.artifact

import jakarta.persistence.*
import org.bukkit.Location
import kotlin.jvm.Transient

@Entity
class Artifact {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Specifications::class)
    var consumers: List<Specifications> = listOf()

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Specifications::class)
    var producers: List<Specifications> = listOf()

    @Transient
    var flow: Float = 0f

    fun activate(entity: org.bukkit.entity.Entity, location: Location){
        producers.map { it.produce(entity, location) }.sumOf { it.toDouble() }
        if (consumers.isNotEmpty()){
            val flowValue = flow / consumers.size
            consumers.forEach { it.consume(entity, location, flowValue) }
        }
    }
}