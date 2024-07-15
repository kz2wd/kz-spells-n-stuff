package com.cludivers.kz2wdprison.attributes.commands

import com.cludivers.kz2wdprison.attributes.AttributeItem
import com.cludivers.kz2wdprison.commands.SubCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AttributeItemUnfill(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.isEmpty()) {
            return false
        }
        val removing = args[0].toIntOrNull() ?: 0

        AttributeItem.removeShards(sender.inventory.itemInMainHand, removing)

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