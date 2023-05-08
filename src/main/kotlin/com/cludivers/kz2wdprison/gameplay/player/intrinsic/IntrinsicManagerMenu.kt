package com.cludivers.kz2wdprison.gameplay.player.intrinsic

import com.cludivers.kz2wdprison.framework.persistance.beans.player.IntrinsicAttributes
import com.cludivers.kz2wdprison.gameplay.menu.ChoiceMenu
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

class IntrinsicManagerMenu(private val session: Session): ChoiceMenu({ Component.text("???")}, 9 * 5), CommandExecutor {

    private fun changeName(itemStack: ItemStack, newName: String): ItemStack{
        val itm = itemStack.itemMeta
        itm.displayName(Component.text(newName))
        val itCopy = itemStack.clone()
        itCopy.itemMeta = itm
        return itCopy
    }

    override fun generateChoices(player: Player): Map<Int, Pair<ItemStack, () -> Unit>> {
        val playerData = player.getData(session)
        val attributesItemStacks = mapOf(
            IntrinsicAttributes.VIGOR to ItemStack(Material.APPLE),
            IntrinsicAttributes.TOUGHNESS to ItemStack(Material.CHAINMAIL_CHESTPLATE),
            IntrinsicAttributes.AGILITY to ItemStack(Material.FEATHER),
            IntrinsicAttributes.STRENGTH to ItemStack(Material.IRON_AXE)
        )
        
        val inc = attributesItemStacks.entries.mapIndexed {
                index, entry -> index * 9 to Pair(changeName(entry.value, "Increase ${entry.key.name}"),
                    { playerData.intrinsic.increaseAttribute(session, entry.key) }) }.toMap()
        val dec = attributesItemStacks.entries.mapIndexed {
                index, entry -> (index + 1) * 9 - 1 to Pair(changeName(entry.value, "Decrease ${entry.key.name}"),
            { playerData.intrinsic.decreaseAttribute(session, entry.key) }) }.toMap()

        return inc + dec
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player){
            return false
        }

        this.open(sender)

        return true
    }
}