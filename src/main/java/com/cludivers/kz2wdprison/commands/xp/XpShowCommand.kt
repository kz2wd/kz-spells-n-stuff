package com.cludivers.kz2wdprison.commands.xp

import com.cludivers.kz2wdprison.beans.PlayerBean
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class XpShowCommand(private val session: Session) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }
        val playerData = PlayerBean.getPlayerInfo(sender, session)

        sender.sendMessage(Component.text("Vous disposez de ${playerData.skillPoint} PC"))

        return true
    }
}