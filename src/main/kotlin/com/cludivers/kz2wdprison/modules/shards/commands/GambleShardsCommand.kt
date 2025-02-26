package com.cludivers.kz2wdprison.modules.shards.commands

import com.cludivers.kz2wdprison.framework.commands.MainCommandNames
import com.cludivers.kz2wdprison.framework.commands.ServerCommand
import com.cludivers.kz2wdprison.framework.commands.SubCommand
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.modules.player.tryTakeShards
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Suppress("unused")
@ServerCommand("gamble", MainCommandNames.SHARDS)
class GambleShardsCommand: SubCommand(MainCommandNames.SHARDS) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            return false
        }
        val playerShards = sender.getData().shards
        val pulled = sender.tryTakeShards(PULLING_COST.toDouble())
        if (!pulled) {
            sender.sendMessage(Component.text("You don't have enough shards to pull (${playerShards}/$PULLING_COST)").color(
                NamedTextColor.RED))
            return false
        }

        val quantity = Random.nextInt(1, 64)
        val material = Material.values().random()
        val itemPulled = ItemStack(material, quantity)
        sender.sendMessage(Component.text("You pulled $quantity $material").color(
            NamedTextColor.WHITE))
        sender.inventory.addItem(itemPulled)


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

    companion object {
        private const val PULLING_COST = 100
    }
}