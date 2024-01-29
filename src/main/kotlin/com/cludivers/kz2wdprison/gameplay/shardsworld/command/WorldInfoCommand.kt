package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.WorldState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class WorldInfoCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val worldState: WorldState? = WorldState.getWorldState(sender.world)
        if (worldState == null) {
            sender.sendMessage(
                Component.text("Your current world has no information attached").color(NamedTextColor.YELLOW)
            )
            return false
        }

        sender.sendMessage(worldState.showInfo())

        return false
    }
}