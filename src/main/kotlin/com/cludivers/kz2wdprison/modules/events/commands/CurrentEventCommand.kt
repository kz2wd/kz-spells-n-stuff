package com.cludivers.kz2wdprison.modules.events.commands

import com.cludivers.kz2wdprison.framework.commands.SubCommand
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class CurrentEventCommand(parentName: String): SubCommand(parentName) {


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player){
            return false
        }

        sender.sendMessage(Component.text())

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