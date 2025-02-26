package com.cludivers.kz2wdprison.modules.artifact.commands

import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.artifact.Artifact
import com.cludivers.kz2wdprison.modules.artifact.ArtifactTriggers
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactAddCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (sender.inventory.itemInMainHand.type == Material.AIR) {
            sender.sendMessage(Component.text("${ChatColor.GRAY}L'air ne peut être liée à rien."))
            return false
        }

        if (args.isEmpty()) {
            sender.sendMessage(usage("create", "trigger_type"))
            return false
        }

        Artifact.createArtifact(sender.inventory.itemInMainHand, ArtifactTriggers.valueOf(args[0]))

        sender.sendMessage(Component.text("${ChatColor.GREEN}Un nouvel artefact à été lié avec l'objet dans votre main !"))

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): MutableList<String> {
        return ArtifactTriggers.values().map { it.name }.toMutableList()
    }
}