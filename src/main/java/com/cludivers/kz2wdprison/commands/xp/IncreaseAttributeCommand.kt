package com.cludivers.kz2wdprison.commands.xp

import com.cludivers.kz2wdprison.attributes.PlayerAttribute
import com.cludivers.kz2wdprison.beans.PlayerBean
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class IncreaseAttributeCommand(private val session: Session, private val attribute: PlayerAttribute): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player){
            return false
        }

        val playerData = PlayerBean.getPlayerInfo(sender, session)
        val transaction = session.beginTransaction()
        var amount = 1
        if (args.isNotEmpty() && args[0].toIntOrNull() != null){
            amount = args[0].toInt()
        }
        attribute.increase(sender, playerData, amount)

        transaction.commit()

        return true
    }
}