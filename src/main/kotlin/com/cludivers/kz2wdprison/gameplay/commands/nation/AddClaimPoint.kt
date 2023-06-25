package com.cludivers.kz2wdprison.gameplay.commands.nation

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.player.getData
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddClaimPoint(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val amount = if (args.isEmpty()) {
            1
        } else {
            args[0].toIntOrNull() ?: 0
        }
        val playerData = sender.getData()
        HibernateSession.session.beginTransaction()
        playerData.nation?.chunkClaimTokens = playerData.nation?.chunkClaimTokens?.plus(amount)
        HibernateSession.session.transaction.commit()
        return true
    }


}