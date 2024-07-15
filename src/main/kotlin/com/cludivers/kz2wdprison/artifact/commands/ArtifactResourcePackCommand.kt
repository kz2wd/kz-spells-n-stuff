package com.cludivers.kz2wdprison.artifact.commands

import com.cludivers.kz2wdprison.commands.SubCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactResourcePackCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        sender.setResourcePack("https://drive.google.com/uc?export=download&id=1QGXOJMP2ueTGWr77PHgswtGlgoZ-Dbwx")

        return true
    }
}