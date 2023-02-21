package com.cludivers.kz2wdprison.menu

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class Menu {

    companion object {
        fun getItem(material: Material, name: Component, lore: List<Component>) : ItemStack {
            val item = ItemStack(material)
            val meta = item.itemMeta
            meta.displayName(name)
            meta.lore(lore)

            item.itemMeta = meta

            return item
        }
    }

    abstract fun generateInventory(player: Player): Inventory
    fun open(player: Player){
        val inventory = generateInventory(player)
        MenuListener.addMenu(inventory, this)
        player.openInventory(inventory)
    }

    fun updateInventory(player: Player){
        val newInventory = generateInventory(player)
        player.openInventory.topInventory.contents = newInventory.contents
    }

    abstract fun handleClick(event: InventoryClickEvent)

}