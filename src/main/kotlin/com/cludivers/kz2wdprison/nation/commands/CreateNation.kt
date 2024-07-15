package com.cludivers.kz2wdprison.nation.commands

import com.cludivers.kz2wdprison.commands.SubCommand
import com.cludivers.kz2wdprison.nation.NationDeclaration
import com.cludivers.kz2wdprison.nation.beans.NationBean.Companion.createNation
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateNation : SubCommand(NationDeclaration.NATION_COMMAND_NAME) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        sender.createNation()
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