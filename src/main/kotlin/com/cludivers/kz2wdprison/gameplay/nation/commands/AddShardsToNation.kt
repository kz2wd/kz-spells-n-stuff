package com.cludivers.kz2wdprison.gameplay.nation.commands

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.framework.persistence.beans.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.nation.NationDeclaration.NATION_COMMAND_NAME
import com.cludivers.kz2wdprison.gameplay.player.requestShards
import com.cludivers.kz2wdprison.gameplay.player.sendErrorMessage
import com.cludivers.kz2wdprison.gameplay.player.sendSuccessMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddShardsToNation : SubCommand(NATION_COMMAND_NAME) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        val nation = sender.getData().nation
        if (nation == null) {
            sender.sendErrorMessage("Vous n'appartenez à aucune nation.")
            return false
        }

        val shardAmount = sender.requestShards(args, 0) ?: return false

        PluginConfiguration.session.beginTransaction()
        nation.shards += shardAmount
        PluginConfiguration.session.transaction.commit()

        sender.sendSuccessMessage("Ajout de $shardAmount framgents à votre nation (maintenant à ${nation.shards} fragments)")

        return true
    }
}