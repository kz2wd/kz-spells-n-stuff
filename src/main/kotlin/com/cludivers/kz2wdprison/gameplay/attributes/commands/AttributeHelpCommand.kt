package com.cludivers.kz2wdprison.gameplay.attributes.commands

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AttributeHelpCommand(parentName: String) : SubCommand(parentName) {


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return false
        }
        val playerData = sender.getData()

        sender.sendMessage(Component.text(playerData.intrinsic.attributes.map { "${it.key} : ${it.value}" }
            .joinToString("\n")))

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