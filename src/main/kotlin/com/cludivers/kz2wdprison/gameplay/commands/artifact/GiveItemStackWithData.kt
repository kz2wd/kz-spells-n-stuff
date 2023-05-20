package com.cludivers.kz2wdprison.gameplay.commands.artifact

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveItemStackWithData(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val itemStackWithData = ItemStack(Material.DIRT)
//        itemStackWithData.editMeta()
        sender.inventory.addItem(itemStackWithData)

        return true
    }
}