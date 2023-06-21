package com.cludivers.kz2wdprison.gameplay.commands.attribute

import com.cludivers.kz2wdprison.framework.persistance.beans.player.AttributeItem
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class AttributeItemAdd(parentName: String, private val session: Session) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (sender.inventory.itemInMainHand.type == Material.AIR) {
            sender.sendMessage(Component.text("${ChatColor.GRAY}L'air ne peut être liée à rien."))
            return false
        }

        if (AttributeItem.isItemStackLinked(sender.inventory.itemInMainHand)) {
            sender.sendMessage(Component.text("${ChatColor.GRAY}Cet objet est déjà lié. Essayez avec un autre."))
            return false
        }

        session.beginTransaction()

        val item = AttributeItem()

        item.linkedItemStack = sender.inventory.itemInMainHand.clone()
        AttributeItem.registerAttributeItem(item, item.linkedItemStack!!)
        sender.sendMessage(Component.text("${ChatColor.GREEN}Un nouvel artefact à été lié avec l'objet dans votre main !"))
        session.persist(item)
        session.transaction.commit()

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