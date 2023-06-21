package com.cludivers.kz2wdprison.gameplay.commands.intrinsic

import com.cludivers.kz2wdprison.framework.persistance.beans.player.AttributeItem
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class AttributeItemInfo(parentName: String, private val session: Session) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val item = AttributeItem.AttributeItems[sender.inventory.itemInMainHand]
        if (item == null) {
            sender.sendMessage(Component.text("Cet objet n'est pas un équipement valide"))
            return false
        }

        sender.sendMessage(Component.text("${item.shardPowerStored} / ${item.maxShardPower}"))

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