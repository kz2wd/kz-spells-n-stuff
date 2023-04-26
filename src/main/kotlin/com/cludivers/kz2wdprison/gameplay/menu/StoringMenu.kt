package com.cludivers.kz2wdprison.gameplay.menu

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

abstract class StoringMenu(private val bankingSlots: Set<Int>): Menu() {

    override fun handleClick(event: InventoryClickEvent) {
        event.isCancelled = !(bankingSlots.contains(event.slot) || event.clickedInventory != event.inventory)
    }

    override fun onItemDrag(event: InventoryDragEvent) {
        event.isCancelled = bankingSlots.containsAll(event.inventorySlots)
    }
}