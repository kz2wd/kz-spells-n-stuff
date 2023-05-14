package com.cludivers.kz2wdprison.gameplay.commands.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Converters
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.InputTypes
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactHelperCommand(parentName: String): SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }

        InputTypes.values().map { Utils.buildItemStack(Component.text("Input : ${it.name}"), Material.DISPENSER, it.customData) }.forEach {
            sender.player?.inventory?.addItem(it)
        }
        Converters.values().map { Utils.buildItemStack(Component.text("Converter : ${it.name}"), Material.HOPPER, it.customData) }.forEach {
            sender.player?.inventory?.addItem(it)
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