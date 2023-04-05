package com.cludivers.kz2wdprison.gameplay.menu

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class ChoiceMenu(val inventoryName: (Player) -> Component,
                 val inventorySize: Int,
                 val update: Boolean = false): Menu() {

    abstract fun generateChoices(player: Player): Map<Int, Pair<ItemStack, () -> Unit>>

    private var choices: Map<Int, Pair<ItemStack, () -> Unit>>? = null
    override fun generateInventory(player: Player): Inventory {
        if (choices === null){
            choices = generateChoices(player)
        }
        val inventory = Bukkit.createInventory(player, inventorySize, inventoryName(player))
        choices!!.entries.forEach { inventory.setItem(it.key, it.value.first) }
        return inventory
    }

    override fun handleClick(event: InventoryClickEvent) {
        choices?.get(event.slot)?.second?.let { it() }
        if (update){
            choices = null
            updateInventory(event.whoClicked as Player)
        }
    }
}