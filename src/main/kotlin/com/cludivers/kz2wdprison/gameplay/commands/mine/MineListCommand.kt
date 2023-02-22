package com.cludivers.kz2wdprison.gameplay.commands.mine

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.world.mines.MineHandler
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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