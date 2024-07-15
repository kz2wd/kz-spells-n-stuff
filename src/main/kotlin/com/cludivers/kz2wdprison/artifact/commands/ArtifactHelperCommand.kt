package com.cludivers.kz2wdprison.artifact.commands

import com.cludivers.kz2wdprison.CustomShardItems
import com.cludivers.kz2wdprison.commands.SubCommand
import com.cludivers.kz2wdprison.utils.Utils.buildItemStack
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactHelperCommand(parentName: String): SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val inventory = Bukkit.createInventory(sender, 9 * 6, Component.text("Runes"))

        CustomShardItems.values().map { it.itemStack }.forEach {
            inventory.addItem(it.asQuantity(32))
        }

        inventory.addItem(buildItemStack(Component.text("Artifact wand"), Material.STICK, 1234567))
        sender.openInventory(inventory)
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