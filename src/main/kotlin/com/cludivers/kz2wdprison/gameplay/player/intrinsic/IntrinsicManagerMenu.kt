package com.cludivers.kz2wdprison.gameplay.player.intrinsic

import com.cludivers.kz2wdprison.gameplay.menu.ChoiceMenu
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IntrinsicManagerMenu: ChoiceMenu({ Component.text("???")}, 9 * 5), CommandExecutor {
    override fun generateChoices(player: Player): Map<Int, Pair<ItemStack, () -> Unit>> {
        return mapOf(1 to Pair(ItemStack(Material.ACACIA_BOAT), { Bukkit.broadcast(Component.text("Bonjour"))}))
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }

        this.open(sender)

        return true
    }
}