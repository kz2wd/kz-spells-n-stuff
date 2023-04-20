package com.cludivers.kz2wdprison.gameplay.artifact

import org.bukkit.Location
import org.bukkit.entity.Entity


interface Producer {
     fun produce(entity: Entity, location: Location): Number
}