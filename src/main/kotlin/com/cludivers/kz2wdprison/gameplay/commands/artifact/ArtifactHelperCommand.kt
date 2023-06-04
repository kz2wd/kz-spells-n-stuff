package com.cludivers.kz2wdprison.gameplay.commands.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.converters.Converters
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.BasicInputRunes
import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactHelperCommand(parentName: String): SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        BasicInputRunes.values()
            .map { Utils.buildItemStack(Component.text("Input : ${it.name}"), Material.DISPENSER, it.customData) }
            .forEach {
                sender.player?.inventory?.addItem(it.asQuantity(32))
            }
        Converters.values()
            .map { Utils.buildItemStack(Component.text("Converter : ${it.name}"), Material.HOPPER, it.customData) }
            .forEach {
                sender.player?.inventory?.addItem(it.asQuantity(32))
            }
        CustomShardItems.values().map { it.itemStack }.forEach {
            sender.player?.inventory?.addItem(it.asQuantity(32))
        }

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