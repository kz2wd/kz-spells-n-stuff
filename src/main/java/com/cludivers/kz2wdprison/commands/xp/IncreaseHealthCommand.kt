package com.cludivers.kz2wdprison.commands.xp

import com.cludivers.kz2wdprison.PlayerBean
import com.cludivers.kz2wdprison.PrisonListener
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class IncreaseHealthCommand(private val session: Session, private val prisonListener: PrisonListener): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player){
            return false
        }

        val playerData = PlayerBean.getPlayerInfo(sender, session)
        val transaction = session.beginTransaction()

        if (playerData.skillPoint > 0){
            playerData.healthLevel += 1
            playerData.skillPoint -= 1

            prisonListener.updatePlayerStats(sender, playerData)

            sender.sendMessage(Component.text("${ChatColor.GREEN} Amélioration de vos PV !"))
        } else {
            sender.sendMessage(Component.text("${ChatColor.RED} Vous n'avez pas assez de point de compétence !"))
        }

        transaction.commit()

        return true
    }
}