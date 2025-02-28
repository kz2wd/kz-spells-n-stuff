package com.cludivers.kz2wdprison.modules.shards.commands

import com.cludivers.kz2wdprison.framework.commands.MainCommandNames
import com.cludivers.kz2wdprison.framework.commands.ServerCommand
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.player.tryPull
import com.cludivers.kz2wdprison.modules.shards.gamble.GambleList.Companion.BASIC_LOOTBOX
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("unused")
@ServerCommand("gamble", MainCommandNames.SHARDS)
class GambleShardsCommand: SubCommand(MainCommandNames.SHARDS) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return false
        }

        return sender.tryPull(BASIC_LOOTBOX)

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