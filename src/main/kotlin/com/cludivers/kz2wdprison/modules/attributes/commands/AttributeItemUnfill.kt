package com.cludivers.kz2wdprison.modules.attributes.commands

import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.attributes.AttributeItem
import com.cludivers.kz2wdprison.modules.player.sendErrorMessage
import com.cludivers.kz2wdprison.modules.player.sendSuccessMessage
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

        var request = args[0].toIntOrNull() ?: 0

        val shardsInItem = AttributeItem.getShardsAmount(sender.inventory.itemInMainHand)
        if (request > shardsInItem){
            request = shardsInItem
        }

        sender.sendSuccessMessage("Took $request shards from equipment")

        AttributeItem.removeShards(sender.inventory.itemInMainHand, request)

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