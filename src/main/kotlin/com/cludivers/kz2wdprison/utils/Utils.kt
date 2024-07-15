package com.cludivers.kz2wdprison.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.logging.Level
import java.util.logging.Logger

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
    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun <T> debug(any: T): T {
        Logger.getGlobal().log(Level.INFO, any.toString())
        return any
    }

}