package com.cludivers.kz2wdprison.modules.nation.commands

import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.nation.NationCoreEntity
import com.cludivers.kz2wdprison.modules.nation.NationDeclaration
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.modules.player.sendErrorMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnNationCore: SubCommand(NationDeclaration.NATION_COMMAND_NAME) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player){
            return false
        }

        val nation = sender.getData().nation
        if (nation == null) {
            sender.sendErrorMessage("You must belong to a nation.")
            return false
        }

        NationCoreEntity(nation, sender.location)

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