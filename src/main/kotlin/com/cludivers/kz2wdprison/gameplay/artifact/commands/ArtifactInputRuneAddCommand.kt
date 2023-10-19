package com.cludivers.kz2wdprison.gameplay.artifact.commands

import com.cludivers.kz2wdprison.gameplay.artifact.beans.ArtifactComplexRune
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactRuneTypes
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactInputRuneAddCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (sender.inventory.itemInMainHand.type == Material.AIR) {
            sender.sendMessage(Component.text("${ChatColor.GRAY}L'air ne peut être liée à rien."))
            return false
        }

        ArtifactComplexRune.createComplexRune(sender.inventory.itemInMainHand, ArtifactRuneTypes.GENERIC_INPUT_RUNE)
        sender.sendMessage(Component.text("${ChatColor.GREEN}Une nouvelle source d'artefact à été lié avec l'objet dans votre main !"))


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