package com.cludivers.kz2wdprison.commands.xp

import com.cludivers.kz2wdprison.PlayerAttribute
import com.cludivers.kz2wdprison.PlayerBean
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

        attribute.increase(sender, playerData)

        transaction.commit()

        return true
    }
}