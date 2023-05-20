package com.cludivers.kz2wdprison.gameplay.commands.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hibernate.Session

class ArtifactAddCommand(parentName: String, private val session: Session) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (sender.inventory.itemInMainHand.type == Material.AIR) {
            sender.sendMessage(Component.text("${ChatColor.GRAY}L'air ne peut être liée à rien."))
            return false
        }

        if (ArtifactListener.isItemStackLinked(sender.inventory.itemInMainHand)) {
            sender.sendMessage(Component.text("${ChatColor.GRAY}Cet objet est déjà lié. Essayez avec un autre."))
            return false
        }

        session.beginTransaction()

        val artifact = Artifact()
        artifact.linkedItemStack = sender.inventory.itemInMainHand.clone()
        ArtifactListener.registerArtifact(artifact, artifact.linkedItemStack!!)
        sender.sendMessage(Component.text("${ChatColor.GREEN}Un nouvel artefact à été lié avec l'objet dans votre main !"))
        session.persist(artifact)
        session.transaction.commit()

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