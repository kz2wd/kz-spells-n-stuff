package com.cludivers.kz2wdprison.framework.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

abstract class SubCommand(val parentName: String): CommandExecutor, TabExecutor {

    constructor(mainCommandNames: MainCommandNames): this(mainCommandNames.name)

    fun usage(cmdName: String, cmdArgs: String): Component {
        return Component.text("/${parentName} $cmdName $cmdArgs").color(NamedTextColor.YELLOW)
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