package com.cludivers.kz2wdprison.framework.beans.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.Consumer
import com.cludivers.kz2wdprison.gameplay.artifact.Producer
import jakarta.persistence.Entity
import org.bukkit.Location

enum class Specifications: Consumer, Producer {

    SHARDS_ATTRACTION,
    BLOCKS,
    PROJECTILE;
    override fun consume(entity: org.bukkit.entity.Entity, location: Location, flow: Number) {
        // Default behavior : do nothing
    }

    override fun produce(entity: org.bukkit.entity.Entity, location: Location): Number {
        // Default behavior : do nothing
        return 0
    }

}