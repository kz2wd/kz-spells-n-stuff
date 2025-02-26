package com.cludivers.kz2wdprison.modules.attributes.commands

import com.cludivers.kz2wdprison.modules.attributes.AttributeItem
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AttributeItemFill(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.isEmpty()) {
            return false
        }
        val adding = args[0].toIntOrNull() ?: 0

        AttributeItem.addShards(sender.inventory.itemInMainHand, adding)

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