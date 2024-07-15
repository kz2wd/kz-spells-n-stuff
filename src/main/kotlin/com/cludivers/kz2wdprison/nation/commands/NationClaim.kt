package com.cludivers.kz2wdprison.nation.commands

import com.cludivers.kz2wdprison.commands.SubCommand
import com.cludivers.kz2wdprison.nation.NationDeclaration
import com.cludivers.kz2wdprison.nation.beans.NationBean.Companion.claimChunk
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NationClaim : SubCommand(NationDeclaration.NATION_COMMAND_NAME) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage(Component.text("UPDATE"))
        Bukkit.broadcast(Component.text("AAAAAAAAAAAAAAA"))
        if (sender !is Player) {
            sender.sendMessage(Component.text("OOUT"))
            return false
        }
        sender.claimChunk()
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