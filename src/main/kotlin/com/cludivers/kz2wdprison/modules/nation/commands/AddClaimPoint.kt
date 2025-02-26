package com.cludivers.kz2wdprison.modules.nation.commands

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.nation.NationDeclaration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddClaimPoint : SubCommand(NationDeclaration.NATION_COMMAND_NAME) {
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
        PluginConfiguration.session.beginTransaction()
        playerData.nation?.chunkClaimTokens = playerData.nation?.chunkClaimTokens?.plus(amount) ?: 0
        PluginConfiguration.session.transaction.commit()
        return true
    }


}