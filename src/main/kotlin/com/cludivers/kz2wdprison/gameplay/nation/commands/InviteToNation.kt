package com.cludivers.kz2wdprison.gameplay.nation.commands

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.nation.beans.NationBean.Companion.invitePlayerToNation
import com.cludivers.kz2wdprison.gameplay.player.sendErrorMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class InviteToNation(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.isEmpty()) {
            sender.sendErrorMessage("Vous devez préciser le nom de la personne à inviter")
            return false
        }

        val invitedPlayer = Bukkit.getPlayer(args[0])
        if (invitedPlayer !is Player){
            sender.sendErrorMessage("Le joueur ${args[0]} n'existe pas ou n'est pas connecté")
            return false
        }

        sender.invitePlayerToNation(invitedPlayer)
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