package com.cludivers.kz2wdprison.nation.commands

import com.cludivers.kz2wdprison.persistence.beans.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.commands.SubCommand
import com.cludivers.kz2wdprison.nation.NationDeclaration
import com.cludivers.kz2wdprison.nation.beans.NationAttack
import com.cludivers.kz2wdprison.nation.beans.NationBean
import com.cludivers.kz2wdprison.nation.beans.NationBean.Companion.getNationMatching
import com.cludivers.kz2wdprison.player.requestShards
import com.cludivers.kz2wdprison.player.sendErrorMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AttackNation : SubCommand(NationDeclaration.NATION_COMMAND_NAME) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.isEmpty()) {
            sender.sendErrorMessage("Vous devez préciser la nation que vous voulez attaquer.")
            return false
        }

        val targetName = args.drop(1).joinToString(" ")
        val targetNation = NationBean.getNationFromName(targetName)
        if (targetNation == null) {
            sender.sendErrorMessage("La nation $targetName n'existe pas.")
            return false
        }

        val shardAmount = sender.requestShards(args, 0) ?: return false

        NationAttack.createAttack(targetNation, sender.getData(), shardAmount)

        return true

    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
        if (args.isEmpty()) {
            return listOf("amount").toMutableList()
        }
        return getNationMatching(args.drop(1).joinToString(" ")).keys.toMutableList()
    }

}