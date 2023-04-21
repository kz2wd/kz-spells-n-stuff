package com.cludivers.kz2wdprison.gameplay.artifact

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.hibernate.Session


interface Consumer {
    fun consume(session: Session, item: ItemStack, entity: Entity, location: Location, flow: Float): Unit
}