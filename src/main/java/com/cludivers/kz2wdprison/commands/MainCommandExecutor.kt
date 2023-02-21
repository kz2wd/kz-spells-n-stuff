package com.cludivers.kz2wdprison.commands

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class MainCommandExecutor(private val commands: Map<String, SubCommand>, private val mainCommand: CommandExecutor) :
    CommandExecutor, TabExecutor {

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

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        Bukkit.broadcast(Component.text(args.joinToString(separator = " ") { it }))
        if (args.isEmpty() || args[0].isBlank()){
            Bukkit.broadcast(Component.text(commands.keys.joinToString(separator = " ") { it }))
            return commands.keys.toMutableList()
        } else if (commands.containsKey(args[0])){
            return commands[args[0]]!!.onTabComplete(sender, command, args[0], args.drop(1).toTypedArray())
        }
        return null

    }
}