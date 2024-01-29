package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.WorldGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class WorldGeneratorCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Component.text("Specify a name").color(NamedTextColor.RED))
            return false
        }
        val name = args[0]
        sender.sendMessage(Component.text("Starting world generation . . .").color(NamedTextColor.GREEN))
        try {
            WorldGenerator.generateNewWorld(name)
        } catch (e: Exception) {
            sender.sendMessage(Component.text("An error occurred : ${e.message}").color(NamedTextColor.RED))
            return false
        }

        return true
    }


}