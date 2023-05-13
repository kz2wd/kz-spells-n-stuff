package com.cludivers.kz2wdprison.gameplay.utils

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Utils {

    fun buildItemStack(name: Component, material: Material, customData: Int): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.setCustomModelData(customData)
        meta.displayName(name)
        item.setItemMeta(meta)
        return item
    }

}