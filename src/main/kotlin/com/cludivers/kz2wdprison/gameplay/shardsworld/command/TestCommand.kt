package com.cludivers.kz2wdprison.gameplay.shardsworld.command

import com.cludivers.kz2wdprison.gameplay.commands.SubCommand
import com.cludivers.kz2wdprison.gameplay.shardsworld.WorldGenerator
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.block.BlockTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand(parentName: String) : SubCommand(parentName) {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.size < 4) {
            sender.sendMessage(usage(label, "x y z size"))
            return false
        }

        try {
            val x = args[0].toInt()
            val y = args[1].toInt()
            val z = args[2].toInt()
            val size = args[3].toInt()

            WorldGenerator.generateCylinder(BlockVector3.at(x, y, z), size.toDouble(), BlockTypes.STONE!!, sender.world)

            sender.sendMessage(
                Component.text("Successfully generated a sphere at $x $y $z of size $size !")
                    .color(NamedTextColor.GREEN)
            )

        } catch (e: NumberFormatException) {
            sender.sendMessage(Component.text("Invalid Coordinates !").color(NamedTextColor.RED))
            return false
        }

        return true
    }
}