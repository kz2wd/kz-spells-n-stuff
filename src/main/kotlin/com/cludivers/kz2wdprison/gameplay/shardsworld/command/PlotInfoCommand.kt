package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.PlotState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class PlotInfoCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        sender.sendMessage(Component.text("All plots :\n").color(NamedTextColor.GREEN))
        PlotState.getAllPLots()
            .forEach { sender.sendMessage(Component.text("${it.plotName} (${it.plotX}, ${it.plotZ})")) }

        val plotState: PlotState? = PlotState.getPlotState(sender.location)
        if (plotState == null) {
            sender.sendMessage(
                Component.text("Your current land has no information attached").color(NamedTextColor.YELLOW)
            )
            return false
        }

        sender.sendMessage(plotState.showInfo())

        return false
    }
}