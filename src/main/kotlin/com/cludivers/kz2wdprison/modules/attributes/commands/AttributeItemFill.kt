package com.cludivers.kz2wdprison.modules.attributes.commands

import com.cludivers.kz2wdprison.modules.attributes.AttributeItem
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.modules.player.requestShards
import com.cludivers.kz2wdprison.modules.player.sendErrorMessage
import com.cludivers.kz2wdprison.modules.player.sendSuccessMessage
import com.cludivers.kz2wdprison.modules.player.tryTakeShards
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
        var request = args[0].toIntOrNull() ?: 0

        val max = AttributeItem.getMissingShardsAmount(sender.inventory.itemInMainHand)
        if (request > max) {
            request = max
        }


        val accepted = sender.tryTakeShards(request.toDouble())
        if (!accepted) {
            sender.sendErrorMessage("You don't have enough shards")
            return false
        }

        sender.sendSuccessMessage("Filled equipment with $request shards")
        AttributeItem.addShards(sender.inventory.itemInMainHand, request)

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