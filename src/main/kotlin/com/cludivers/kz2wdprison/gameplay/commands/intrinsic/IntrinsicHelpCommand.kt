package com.cludivers.kz2wdprison.gameplay.commands.intrinsic

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class IntrinsicHelpCommand(parentName: String, val session: Session) : SubCommand(parentName) {


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return false
        }
        val playerData = sender.getData(session)

        sender.sendMessage(Component.text(playerData.intrinsic.attributes.map { "${it.key} : ${it.value}" }
            .joinToString("\n")))

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