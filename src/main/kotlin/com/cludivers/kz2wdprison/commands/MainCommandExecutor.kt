package com.cludivers.kz2wdprison.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class MainCommandExecutor(private val commands: Map<String, SubCommand>) :
    CommandExecutor, TabExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.isNotEmpty() && args[0] in commands) {
            commands[args[0]]?.onCommand(sender, command, args[0], args.drop(1).toTypedArray())
        } else {
            val commandsUsage = commands.map { it.value.usage(it.key, "") }.fold(Component.text("")) { acc, i ->
                acc.appendNewline().append(i) as TextComponent
            }
            sender.sendMessage(
                Component.text(
                    "${ChatColor.YELLOW}Liste des commandes disponibles :"
                ).append(commandsUsage)
            )
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        if (commands.containsKey(args[0])) {
            return commands[args[0]]!!.onTabComplete(sender, command, args[0], args.drop(1).toTypedArray())
        } else if (args.size < 2) {
            return commands.keys.toMutableList()
        }
        return null

    }
}