package com.cludivers.kz2wdprison.gameplay.attributes

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import com.cludivers.kz2wdprison.gameplay.listeners.PrisonListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
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

    fun increase(player: Player, playerData: PlayerBean, amount: Int){
        for (i in 0 until amount) {

            if (maxLevel > 0 && getAttributeLevel(playerData) >= maxLevel){
                player.sendActionBar(Component.text("${ChatColor.GREEN}${ChatColor.BOLD}Cet attribut est à son niveau maximum"))
                return
            }
            val cost = costFunction(playerData)

            if (cost <= playerData.skillPoint){
                increaseFunction(playerData)
                playerData.skillPoint -= cost
                PrisonListener.updatePlayerStats(player, playerData)
                PrisonListener.updatePlayerPickaxe(player, playerData, true)
                player.sendActionBar(successMessage)
            } else {
                player.sendActionBar(Component.text("${ChatColor.RED}Vous n'avez pas assez de point de compétence !"))
                return
            }
        }
    }

    fun upgradeComponent(playerData: PlayerBean): Pair<Component, Component>{
        if (maxLevel > 0 &&getAttributeLevel(playerData) >= maxLevel){
            return Pair(Component.text("$attributeName est au niveau maximum (${maxLevel})"), Component.text(""))
        }
        val cost = costFunction(playerData)

        var upgradeComponent =  Component.text("Augmenter $attributeName").decorate(TextDecoration.BOLD)
        upgradeComponent = upgradeComponent.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
            "/$parentCommandName $increaseCommandCallName"))


        val costIndication = Component.text("     ($cost PC)")

        val loreInformation = Component.text(
            "${attributeValueAtLevel(getAttributeLevel(playerData))} " +
                    "=> ${attributeValueAtLevel(getAttributeLevel(playerData) + 1)} ")


        upgradeComponent = upgradeComponent.append(costIndication)
        return Pair(upgradeComponent, loreInformation)
    }
}