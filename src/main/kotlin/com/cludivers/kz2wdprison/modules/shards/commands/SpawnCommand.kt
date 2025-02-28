package com.cludivers.kz2wdprison.modules.shards.commands

import com.cludivers.kz2wdprison.Kz2wdPrison.Companion.MAIN_WORLD
import com.cludivers.kz2wdprison.framework.commands.MainCommandNames
import com.cludivers.kz2wdprison.framework.commands.ServerCommand
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.shardsworld.PlotState
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("unused")
@ServerCommand("spawn", MainCommandNames.SHARDS)
class SpawnCommand: SubCommand(MainCommandNames.SHARDS) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        sender.teleport(PlotState.SPAWN_PLOT.getSpawnLocation(MAIN_WORLD)!!)

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
        return mutableListOf()
    }
}