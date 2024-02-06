package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.PlotState
import com.cludivers.kz2wdprison.gameplay.shardsworld.WorldGenerator
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RegenerateCurrentPlotCommand(parentName: String) : SubCommand(parentName) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }


        val plot = PlotState.getPlotState(sender.location)

        if (plot == null) {
            sender.sendMessage(Component.text("Generating a new world").color(NamedTextColor.YELLOW))
            WorldGenerator.generateNewPlot(Utils.getRandomString(8))
            return true
        }

        sender.sendMessage(Component.text("Regenerating plot").color(NamedTextColor.GREEN))
        WorldGenerator.generatePlotTerrain(plot)

        return true
    }

}

