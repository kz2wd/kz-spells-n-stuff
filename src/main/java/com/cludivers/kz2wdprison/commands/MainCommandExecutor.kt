package com.cludivers.kz2wdprison.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MainCommandExecutor(private val commands: Map<String, CommandExecutor>, private val mainCommand: CommandExecutor) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }

        if (args.isNotEmpty() && args[0] in commands){
            commands[args[0]]?.onCommand(sender, command, label, args.drop(1).toTypedArray())
        } else {
            mainCommand.onCommand(sender, command, label, arrayOf())
        }

        return true
    }
}