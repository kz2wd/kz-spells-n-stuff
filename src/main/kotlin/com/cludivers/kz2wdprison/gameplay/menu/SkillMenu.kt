package com.cludivers.kz2wdprison.gameplay.menu

import com.cludivers.kz2wdprison.gameplay.attributes.PlayerAttribute
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.hibernate.Session

class SkillMenu(private val session: Session, private val attributes: List<Pair<Material, PlayerAttribute>>): CommandExecutor, Menu() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }

        this.open(sender)

        return true
    }

    override fun generateInventory(player: Player): Inventory {
        val transaction = session.beginTransaction()
        val playerData = player.getData(session)
        val inventory = Bukkit.createInventory(player, 9 * 1, Component.text("Amélioration de vos compétences"))
        attributes.withIndex().forEach {
            val infos = it.value.second.upgradeComponent(playerData)
            inventory.setItem(it.index * 2, getItem(it.value.first, infos.first,
                listOf(infos.second)))
        }
        transaction.commit()
        return inventory
    }

    override fun handleClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        when(event.slot){
            0 -> player.chat("/xp pickaxe")
            2 -> player.chat("/xp mining")
            4 -> player.chat("/xp health")
            6 -> player.chat("/xp criticOdd")
            8 -> player.chat("/xp criticFactor")
            else -> Unit
        }

        // Since the menu stays open, update it
        updateInventory(player)
    }
}