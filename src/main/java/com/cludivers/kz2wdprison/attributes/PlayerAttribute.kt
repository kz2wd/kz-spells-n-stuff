package com.cludivers.kz2wdprison.attributes

import com.cludivers.kz2wdprison.PrisonListener
import com.cludivers.kz2wdprison.beans.PlayerBean
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PlayerAttribute(
    private val attributeName: String,
    private val maxLevel: Int,
    val increaseCommandCallName: String,
    private val parentCommandName: String,
    private val getAttributeLevel: (PlayerBean) -> Int,
    private val increaseFunction: (PlayerBean) -> Unit,
    private val costFunction: (PlayerBean) -> (Int),
    private val attributeValueAtLevel: (Int) -> (String),
    private val successMessage: Component
) {

    fun increase(player: Player, playerData: PlayerBean){

        if (maxLevel > 0 && getAttributeLevel(playerData) >= maxLevel){
            player.sendMessage(Component.text("${ChatColor.GREEN}${ChatColor.BOLD}Cet attribut est à son niveau maximum"))
            return
        }

        val cost = costFunction(playerData)

        if (cost <= playerData.skillPoint){
            increaseFunction(playerData)
            playerData.skillPoint -= cost
            PrisonListener.updatePlayerStats(player, playerData)
            PrisonListener.updatePlayerPickaxe(player, playerData, true)
            player.sendMessage(successMessage)
        } else {
            player.sendMessage(Component.text("${ChatColor.RED}Vous n'avez pas assez de point de compétence !"))
        }
    }

    fun upgradeComponent(playerData: PlayerBean): Component{
        if (maxLevel > 0 &&getAttributeLevel(playerData) >= maxLevel){
            return Component.text("$attributeName est au niveau maximum (${maxLevel})")
        }
        val cost = costFunction(playerData)

        var upgradeComponent =  Component.text("Augmenter $attributeName").decorate(TextDecoration.BOLD)
        upgradeComponent = upgradeComponent.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
            "/$parentCommandName $increaseCommandCallName"))

        upgradeComponent = upgradeComponent.hoverEvent(
            HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,  Component.text(
                    "${attributeValueAtLevel(getAttributeLevel(playerData))} " +
                    "=> ${attributeValueAtLevel(getAttributeLevel(playerData) + 1)} ")))

        val costIndication = Component.text("     ($cost PC)").hoverEvent(
            HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                Component.text("Coût : $cost Point${if (cost > 1) "s" else ""} de Compétence")))

        upgradeComponent = upgradeComponent.append(costIndication)
        return upgradeComponent
    }
}