package com.cludivers.kz2wdprison.gameplay.commands.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactTriggers
import com.cludivers.kz2wdprison.gameplay.artifact.DefaultArtifacts
import com.cludivers.kz2wdprison.gameplay.attributes.AttributeItem
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactDemoCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val inventory = Bukkit.createInventory(sender, 9 * 6, Component.text("Artefacts de démonstration"))
        DefaultArtifacts.values().forEach { it.artifact.linkedItemStack?.let { _ -> inventory.addItem(it.artifact.linkedItemStack!!) }  }

        (DefaultArtifacts.values().size + 1 .. DefaultArtifacts.values().size + 10).forEach {
            val item = Utils.buildItemStack(Component.text("Powered armor"), Material.IRON_LEGGINGS)
            AttributeItem.makeAttributeItem(item, 4000, 10000)
            inventory.setItem(it, item)
        }


        sender.openInventory(inventory)

        return true
    }

}