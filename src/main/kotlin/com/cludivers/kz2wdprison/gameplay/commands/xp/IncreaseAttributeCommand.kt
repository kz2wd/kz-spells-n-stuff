package com.cludivers.kz2wdprison.gameplay.commands.xp

import com.cludivers.kz2wdprison.gameplay.attributes.PlayerAttribute
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.player.getData
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class IncreaseAttributeCommand(private val session: Session, private val attribute: PlayerAttribute, parentName: String): SubCommand(
    parentName
) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player){
            return false
        }

        val playerData = sender.getData(session)
        val transaction = session.beginTransaction()
        var amount = 1
        if (args.isNotEmpty() && args[0].toIntOrNull() != null){
            amount = args[0].toInt()
        }
        attribute.increase(sender, playerData, amount)

        transaction.commit()

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String>? {
        return mutableListOf("1", "2", "3", "...")
    }
}