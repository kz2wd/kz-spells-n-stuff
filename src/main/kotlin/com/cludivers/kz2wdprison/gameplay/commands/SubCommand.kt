package com.cludivers.kz2wdprison.gameplay.commands

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

abstract class SubCommand(private val parentName: String): CommandExecutor, TabExecutor {
    fun usage(cmdName: String, cmdArgs: String): Component {
        return Component.text("${ChatColor.YELLOW}/${parentName} $cmdName $cmdArgs")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        return null;
    }
}