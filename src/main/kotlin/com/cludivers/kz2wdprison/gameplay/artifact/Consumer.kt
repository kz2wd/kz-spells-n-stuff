package com.cludivers.kz2wdprison.gameplay.artifact

import org.bukkit.Location
import org.bukkit.entity.Entity


interface Consumer {
    fun consume(entity: Entity, location: Location, flow: Number): Unit
}