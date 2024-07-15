package com.cludivers.kz2wdprison.nation.commands

import com.cludivers.kz2wdprison.commands.SubCommand
import com.cludivers.kz2wdprison.nation.NationDeclaration
import com.cludivers.kz2wdprison.nation.beans.NationBean.Companion.showPendingAttacks
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ShowPendingAttacks : SubCommand(NationDeclaration.NATION_COMMAND_NAME) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        sender.showPendingAttacks()
        return true
    }

}