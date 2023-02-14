package com.cludivers.kz2wdprison

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerExpChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

class PrisonListener(private val session: Session, private val attributes: List<PlayerAttribute>): Listener {

    companion object {
        private val pickaxesTypes = listOf(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)

        private val xpByLevel = listOf(5, 8, 13, 20, 29, 38, 46, 55, 68, 75)

        private fun getXpOfLevel(index: Int): Int{
            val size = xpByLevel.size
            return if (index >= size){
                xpByLevel[size - 1]
            } else {
                xpByLevel[index]
            }

        }
        private fun getUnbreakableItemStack(material: Material): ItemStack{
            val item = ItemStack(material)
            val meta = item.itemMeta
            meta.isUnbreakable = true
            item.itemMeta = meta
            return item
        }

        private fun getEnchantedTool(material: Material, enchants: List<Pair<Enchantment, Int>>): ItemStack{
            val item = getUnbreakableItemStack(material)
            enchants.forEach {
                item.addEnchantment(it.first, it.second)
            }
            return item
        }

        private fun getPlayerPickaxe(playerData: PlayerBean): ItemStack {
            return if (playerData.miningLevel >= 1){
                getEnchantedTool(pickaxesTypes[playerData.pickaxeLevel], listOf(Pair(Enchantment.DIG_SPEED, playerData.miningLevel)))
            } else {
                getUnbreakableItemStack(pickaxesTypes[playerData.pickaxeLevel])
            }
        }

        private fun setPlayerVisualLevel(player: Player, playerData: PlayerBean){
            player.exp = playerData.currentXp.toFloat() / getXpOfLevel(playerData.level)
        }

        private fun getBlockValue(block: Material): Int{
            return when(block) {
                Material.DIRT -> 1
                Material.STONE -> 2
                Material.COAL_ORE -> 3
                Material.IRON_ORE -> 4
                Material.GOLD_ORE -> 10
                Material.REDSTONE_ORE -> 5
                Material.LAPIS_ORE -> 3
                Material.DIAMOND_ORE -> 50
                Material.EMERALD_ORE -> 30
                Material.DEEPSLATE -> 5
                Material.DEEPSLATE_COAL_ORE -> 8
                Material.DEEPSLATE_IRON_ORE -> 10
                Material.DEEPSLATE_GOLD_ORE -> 15
                Material.DEEPSLATE_LAPIS_ORE -> 8
                Material.DEEPSLATE_REDSTONE_ORE -> 10
                Material.DEEPSLATE_DIAMOND_ORE -> 60
                Material.DEEPSLATE_EMERALD_ORE -> 35
                else -> 1
            }
        }

        fun getHealth(level: Int): Double{
            return (level + 1).toDouble() * 2
        }
        fun updatePlayerStats(player: Player, playerData: PlayerBean){
            val playerHealth = getHealth(playerData.healthLevel)
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = playerHealth
            player.health = playerHealth
        }

        fun updatePlayerPickaxe(player: Player, playerData: PlayerBean, notify: Boolean){
            val pickaxe = getPlayerPickaxe(playerData)
            if (! player.inventory.contains(pickaxe)){
                player.inventory.forEach {
                    if (pickaxesTypes.contains(it?.type)) {
                        it?.amount = 0
                    }
                }
                if (notify){
                    player.world.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, .8f)
                }
                player.inventory.addItem(pickaxe)
            }
        }
    }

    private fun playerLevelUpMessage(playerData: PlayerBean): Component {
        val line = Component.text("==============================").color(TextColor.color(100100200))
        val main = Component.text("Niveau ${playerData.level} atteint !").color(TextColor.color(165425554))
        val exp = Component.text("${ChatColor.GREEN}Vous avez ${ChatColor.GOLD}${playerData.skillPoint}" +
                "${ChatColor.GREEN} point${if (playerData.skillPoint > 1) "s" else ""} de compétence.")
        var startContent = line.appendNewline().append(main).appendNewline().append(exp).appendNewline()

        attributes.forEach { startContent = startContent.appendNewline().append(it.upgradeComponent(playerData)) }

        return startContent.appendNewline().append(line)
    }
    private fun playerLevelUpNotification(player: Player, playerData: PlayerBean){
        player.sendMessage(playerLevelUpMessage(playerData))
        player.world.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, .8f)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        val transaction = session.beginTransaction()
        val playerData = PlayerBean.getPlayerInfo(event.player, session)
        if (playerData.connectionAmount < 1) {
            Bukkit.broadcast(Component.text("Bienvenue à ${ChatColor.LIGHT_PURPLE}${event.player.name} ${ChatColor.WHITE} !"))
        } else {
            event.player.sendMessage(Component.text("Rebonjour ${ChatColor.LIGHT_PURPLE}${event.player.name}"))
        }
        playerData.connectionAmount += 1
        updatePlayerStats(event.player, playerData)
        updatePlayerPickaxe(event.player, playerData, false)
        transaction.commit()
    }

    @EventHandler
    fun onPlayerBreak(event: BlockBreakEvent){
        val transaction = session.beginTransaction()

        val playerData = PlayerBean.getPlayerInfo(event.player, session)

        playerData.currentXp += getBlockValue(event.block.type)

        increasePlayerLevel(event.player, playerData)

        setPlayerVisualLevel(event.player, playerData)
        transaction.commit()
    }

    private fun increasePlayerLevel(player: Player, playerData: PlayerBean) {
        while (playerData.currentXp >= getXpOfLevel(playerData.level)) {
            playerData.currentXp -= getXpOfLevel(playerData.level)
            playerData.level += 1
            playerData.skillPoint += 1
            playerLevelUpNotification(player, playerData)
        }
    }

    @EventHandler
    fun onPlayerXpChange(event: PlayerExpChangeEvent){
        event.amount = 0
    }

    @EventHandler
    fun onPlayerStarve(event: FoodLevelChangeEvent){
        event.isCancelled = true
        event.entity.foodLevel = 20
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent){
        val playerData = PlayerBean.getPlayerInfo(event.player, session)
        updatePlayerStats(event.player, playerData)
        updatePlayerPickaxe(event.player, playerData, false)
        event.player.sendMessage(Component.text("${ChatColor.RED}Vous êtes ${ChatColor.BOLD}mort"))
    }

}