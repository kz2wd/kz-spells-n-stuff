package com.cludivers.kz2wdprison.modules.shards.commands

import com.cludivers.kz2wdprison.framework.commands.MainCommandNames
import com.cludivers.kz2wdprison.framework.commands.ServerCommand
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


@Suppress("unused")
@ServerCommand("amount", MainCommandNames.SHARDS)
class CheckShardsAmount: SubCommand(MainCommandNames.SHARDS) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        sender.sendMessage(Component.text("Shards amount: ${sender.getData().shards.toInt()}"))

        return true
    }
}