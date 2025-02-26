package com.cludivers.kz2wdprison.modules.nation.commands

import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.nation.NationDeclaration
import com.cludivers.kz2wdprison.modules.nation.beans.NationBean
import com.cludivers.kz2wdprison.modules.player.sendNotificationMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NationInfo : SubCommand(NationDeclaration.NATION_COMMAND_NAME) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val nation = sender.getData().nation
        if (nation is NationBean) {
            sender.sendNotificationMessage(nation.description())
        } else {
            sender.sendNotificationMessage("Vous n'appartenez à aucune nation.")
            sender.sendNotificationMessage("/nation create pour en créer une")
            sender.sendNotificationMessage("/nation join <inviteur> pour en rejoindre une")
        }

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