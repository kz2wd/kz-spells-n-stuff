package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TravelWorldsCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return false
        }

        if (args.isEmpty()) {
            sender.sendMessage(Component.text("Please specify a destination world.").color(NamedTextColor.RED))
            return false
        }
        val destinationName = args[0]

        val destination = Bukkit.getWorld(destinationName)
        if (destination == null) {
            sender.sendMessage(Component.text("$destinationName is not a valid world.").color(NamedTextColor.RED))
            return false
        }

        sender.sendMessage(Component.text("Teleportation Starting now !").color(NamedTextColor.GREEN))
        sender.teleport(destination.spawnLocation)
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
        return Bukkit.getWorlds().map { it.name }.toMutableList()
    }
}