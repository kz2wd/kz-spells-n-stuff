package com.cludivers.kz2wdprison.gameplay.nation.commands

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.player.acceptNationInvitation
import com.cludivers.kz2wdprison.gameplay.player.sendErrorMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JoinNation(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (args.isEmpty()) {
            sender.sendErrorMessage("Vous devez préciser le nom de la personne qui vous a invité")
            return false
        }

        val invitingPlayer = Bukkit.getOfflinePlayer(args[0]);
        if (invitingPlayer !is Player){
            sender.sendErrorMessage("Le joueur ${args[0]} n'existe pas")
            return false
        }

        sender.acceptNationInvitation(invitingPlayer)
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