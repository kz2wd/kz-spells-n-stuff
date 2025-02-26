package com.cludivers.kz2wdprison.modules.shardsworld.command

import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.framework.utils.Utils
import com.cludivers.kz2wdprison.modules.shardsworld.GenerationShapes
import com.cludivers.kz2wdprison.modules.shardsworld.PlotState
import com.cludivers.kz2wdprison.modules.shardsworld.WorldGenerator
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

        var shape: GenerationShapes? = null

        if (args.isNotEmpty()) {
            shape = GenerationShapes.values().firstOrNull { it.name == args[0] }
        }
        if (shape == null) {
            shape = GenerationShapes.TORUS
        }


        val plot = PlotState.getPlotState(sender.location)
        if (plot == null) {
            sender.sendMessage(Component.text("Generating a new world").color(NamedTextColor.YELLOW))
            WorldGenerator.generateNewPlot(
                Utils.getRandomString(8),
                PlotState.worldLocationToPlotLocation(sender.location),
                shape
            )
            return true
        }



        sender.sendMessage(Component.text("Regenerating plot").color(NamedTextColor.GREEN))
        WorldGenerator.generatePlotTerrain(plot, shape)

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
        return GenerationShapes.values().map { it.name }.toMutableList()
    }
}

