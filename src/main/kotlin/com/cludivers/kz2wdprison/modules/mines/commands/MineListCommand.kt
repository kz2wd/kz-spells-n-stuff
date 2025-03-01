package com.cludivers.kz2wdprison.modules.mines.commands

import com.cludivers.kz2wdprison.framework.commands.MainCommandNames
import com.cludivers.kz2wdprison.framework.commands.ServerCommand
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.mines.MineHandler
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("unused")
@ServerCommand("list", MainCommandNames.SHARDS)
class MineListCommand(private val mineHandler: MineHandler, parentName: String): SubCommand(parentName) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player){
            return false
        }

        val mines = Component.text(mineHandler.allMines.joinToString(separator = " ") { it.name })
        sender.sendMessage(Component.text("Les mines actuellement disponibles sont : ")
            .appendNewline().append(mines))
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