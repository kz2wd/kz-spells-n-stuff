package com.cludivers.kz2wdprison.gameplay.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Utils {


    fun createClickableCommandReference(command: String, description: String): Component {
        return Component.text(command).decorate(TextDecoration.BOLD).color(NamedTextColor.GREEN)
            .clickEvent(ClickEvent.runCommand(command)).append(
                Component.text(description).color(NamedTextColor.WHITE).decorations(
                    mapOf(TextDecoration.BOLD to TextDecoration.State.FALSE)
                )
            )

    }

    fun buildItemStack(name: Component, material: Material, customData: Int? = null): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        if (customData != null){
            meta.setCustomModelData(customData)
        }
        meta.displayName(name)
        item.setItemMeta(meta)
        return item
    }

}