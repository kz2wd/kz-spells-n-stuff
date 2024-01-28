package com.cludivers.kz2wdprison.gameplay.mines.commands

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.mines.MineHandler
import com.cludivers.kz2wdprison.gameplay.mines.PrisonMine
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MineResetCommand(private val mineHandler: MineHandler, parentName: String): SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }

        if (args.isEmpty()){
                sender.sendMessage(usage(label, "<mine>"))
            return false
        }

        val mine = mineHandler.nameToMine[args[0]]
        if (mine !is PrisonMine){
            sender.sendMessage(Component.text("${ChatColor.YELLOW}La mine ${args[0]} n'existe pas"))
            return false
        }

        sender.sendMessage(Component.text("${ChatColor.GREEN}La mine ${args[0]} à été réinitialisée !"))
        mine.reset()

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>):
            MutableList<String>? {
        return mineHandler.allMines.map { it.name }.toMutableList()
    }
}