package com.cludivers.kz2wdprison.gameplay.menu

import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

abstract class StoringMenu(private val bankingSlots: Set<Int>, private val enableShiftMove: Boolean = false): Menu() {

    override fun handleClick(event: InventoryClickEvent) {
        if (event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY && !enableShiftMove){
            // Well it would be great if we could choose the filled slot with this, but, it is too much trouble for what it's worth
            return
        }
        event.isCancelled = !(bankingSlots.contains(event.slot) || event.clickedInventory != event.inventory)
    }

    override fun onItemDrag(event: InventoryDragEvent) {
        event.isCancelled = !( bankingSlots.containsAll(event.inventorySlots) || !event.rawSlots.any { it in event.inventorySlots })
    }
}