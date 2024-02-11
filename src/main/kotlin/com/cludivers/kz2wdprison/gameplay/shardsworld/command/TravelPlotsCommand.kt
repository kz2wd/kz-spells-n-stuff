package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.PlotState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TravelPlotsCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return false
        }

        val destX: Int?
        val destZ: Int?
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("Using current coordinates").color(NamedTextColor.GREEN))
            val inWorld = PlotState.worldLocationToPlotLocation(sender.location)
            destX = inWorld.first
            destZ = inWorld.second
        } else {
            if (args.size < 2) {
                sender.sendMessage(Component.text("Please specify a (X, Z) location.").color(NamedTextColor.RED))
                return false
            }
            destX = args[0].toIntOrNull()
            destZ = args[1].toIntOrNull()
            if (destX == null || destZ == null) {
                sender.sendMessage(Component.text("Invalid coordinate number").color(NamedTextColor.RED))
                return false
            }
        }

        val destination = PlotState.getPlotFromPlotLocation(destX, destZ)
        if (destination == null) {
            sender.sendMessage(Component.text("No land at given location.").color(NamedTextColor.RED))
            return false
        }

        sender.sendMessage(Component.text("Teleportation Starting now !").color(NamedTextColor.GREEN))
        val destinationLocation = destination.getSpawnLocation(Bukkit.getWorld("world")!!)
        if (destinationLocation == null) {
            // should never happen but just in case
            sender.sendMessage(Component.text("Provided destination has no valid location, impossible to teleport"))
            return false
        }
        sender.teleport(destinationLocation)
        return true
    }
}