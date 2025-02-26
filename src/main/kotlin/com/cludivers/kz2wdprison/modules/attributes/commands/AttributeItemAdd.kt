package com.cludivers.kz2wdprison.modules.attributes.commands

import com.cludivers.kz2wdprison.modules.attributes.AttributeItem
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AttributeItemAdd(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (sender.inventory.itemInMainHand.type == Material.AIR) {
            sender.sendMessage(Component.text("L'air ne peut être liée à rien.").color(NamedTextColor.GRAY))
            return false
        }

        AttributeItem.makeAttributeItem(sender.inventory.itemInMainHand)
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        return null
    }
}