package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.attributes.PlayerAttribute
import com.cludivers.kz2wdprison.beans.PlayerBean
import com.cludivers.kz2wdprison.beans.ores.OreStats
import com.cludivers.kz2wdprison.beans.ores.Ores
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEvent.hoverEvent
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
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.hibernate.Session
import java.util.*
import kotlin.random.Random

class PrisonListener(private val plugin: JavaPlugin, private val session: Session, private val attributes: List<PlayerAttribute>): Listener {

    companion object {
        private val pickaxesTypes = listOf(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)

        private val xpByLevel = listOf(5, 8, 13, 20, 29, 38, 46, 55, 68, 75, 100, 125, 150, 175, 200, 225, 250,
            250, 250, 250, 250, 250, 250, 275, 275, 275, 275, 275, 300, 300, 300, 300, 300, 350, 350, 350, 350,
            400, 400, 400, 400, 400, 500, 500, 500, 600, 600, 600, 700, 700, 700, 800, 800, 800, 900, 900, 900,
            1000)

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
                item.addUnsafeEnchantment(it.first, it.second)
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
            player.exp = playerData.currentXp / getXpOfLevel(playerData.level)
        }

        private fun getInRange(min: Int, max: Int): Pair<Float, Float>{
            val scale = Random.nextFloat()
            return Pair(min + scale * (max - min), scale)
        }

        private fun getBlockValue(block: Material, playerData: PlayerBean): Pair<Float, Float>{
            return when(block) {
                Material.STONE -> getInRange(Ores.STONE.minXp, playerData.oresStats.stone.maxXp)
                Material.COAL_ORE -> getInRange(Ores.COAL.minXp, playerData.oresStats.coal.maxXp)
                Material.IRON_ORE -> getInRange(Ores.IRON.minXp, playerData.oresStats.iron.maxXp)
                Material.GOLD_ORE -> getInRange(Ores.GOLD.minXp, playerData.oresStats.gold.maxXp)
                Material.REDSTONE_ORE -> getInRange(Ores.REDSTONE.minXp, playerData.oresStats.redstone.maxXp)
                Material.LAPIS_ORE -> getInRange(0, 10)
                Material.DIAMOND_ORE -> getInRange(Ores.DIAMOND.minXp, playerData.oresStats.diamond.maxXp)
                Material.EMERALD_ORE -> getInRange(0, 30)
                Material.DEEPSLATE -> getInRange(Ores.STONE.minXp, playerData.oresStats.stone.maxXp + 5)
                Material.DEEPSLATE_COAL_ORE -> getInRange(Ores.COAL.minXp, playerData.oresStats.coal.maxXp + 5)
                Material.DEEPSLATE_IRON_ORE -> getInRange(Ores.IRON.minXp, playerData.oresStats.iron.maxXp + 5)
                Material.DEEPSLATE_GOLD_ORE -> getInRange(Ores.GOLD.minXp, playerData.oresStats.gold.maxXp + 5)
                Material.DEEPSLATE_LAPIS_ORE -> getInRange(0, 10)
                Material.DEEPSLATE_REDSTONE_ORE -> getInRange(Ores.REDSTONE.minXp, playerData.oresStats.redstone.maxXp + 5)
                Material.DEEPSLATE_DIAMOND_ORE -> getInRange(Ores.DIAMOND.minXp, playerData.oresStats.diamond.maxXp)
                Material.DEEPSLATE_EMERALD_ORE -> getInRange(0, 150)
                else -> getInRange(0, 1)
            }
        }

        fun getHealth(level: Int): Double{
            return (level + 1).toDouble() * 2
        }

        fun getCriticOdd(level: Int): Float {
            return (level + 1) / (10 + (level + 1)).toFloat()
        }

        fun getCriticFactor(level: Int): Int {
            return (level + 2)
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

    private fun getColorEffect(scale: Float): String{
        return if (scale < .2){
            "${ChatColor.DARK_GRAY}"
        } else if (scale < .4){
            "${ChatColor.GRAY}"
        } else if (scale < .8){
            "${ChatColor.GREEN}"
        } else {
            "${ChatColor.BOLD}${ChatColor.GOLD}"
        }
    }
    @EventHandler
    fun onPlayerBreak(event: BlockBreakEvent){
        val transaction = session.beginTransaction()

        val playerData = PlayerBean.getPlayerInfo(event.player, session)

        var (xpGained, scale) = getBlockValue(event.block.type, playerData)
        var colorEffect = getColorEffect(scale)
        // Try to do a critic Hit
        if (Random.nextFloat() < getCriticOdd(playerData.criticOddLevel)){
            colorEffect = "${ChatColor.LIGHT_PURPLE}"
            xpGained *= getCriticFactor(playerData.criticFactorLevel)
            event.player.world.playSound(event.player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, .0f)
        }

        event.player.sendActionBar(Component.text("${colorEffect}+ ${"%.1f".format(xpGained)}"))
        playerData.currentXp += xpGained

        increasePlayerLevel(event.player, playerData)
        checkAchievements(event.player, playerData, event.block.type)
        setPlayerVisualLevel(event.player, playerData)
        transaction.commit()
    }


    private fun applyAchievement(player: Player, stats: OreStats, amount: List<Int>, name: String, action: String, materials: String) {
        val base = Component.text("${ChatColor.GOLD}${ChatColor.BOLD}SUCCES OBTENU ")
        val indexToChar = listOf("I", "II", "II", "IV", "V", "VI", "VII", "VIII", "IX", "X")
        amount.withIndex().forEach{
            if (it.value == stats.amountMined){
                val achiev = Component.text("$name ${indexToChar[it.index]}")
                val hover = hoverEvent(HoverEvent.Action.SHOW_TEXT,  Component.text("$action ${amount[it.index]} $materials"))
                player.sendMessage(base.append(achiev).hoverEvent(hover))
                val award = Component.text("${ChatColor.GREEN}Récompense : Xp obtenu en minant des ${ChatColor.RESET}$materials ${ChatColor.BOLD}augmenté !")
                val awardHover = hoverEvent(HoverEvent.Action.SHOW_TEXT,  Component.text("XP max : ${stats.maxXp} => ${stats.maxXp * 2}"))
                player.sendMessage(award.hoverEvent(awardHover))
                player.world.playSound(player, Sound.ENTITY_TNT_PRIMED, 1f, .0f)
                stats.maxXp *= 2
            }
        }
    }

    private fun checkAchievements(player: Player, playerData: PlayerBean, brokeBlock: Material){
        val oreStats = playerData.oresStats.getOreInfos(brokeBlock)
        oreStats?.let {
            it.amountMined += 1
            when (brokeBlock) {
                Material.STONE, Material.DEEPSLATE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Pierre",
                        "Miner",
                        "pierres ou pierres des abîmes"
                    )
                }
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Charbon",
                        "Miner",
                        "minerais de charbon"
                    )
                }

                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Fer",
                        "Miner",
                        "minerais de fer"
                    )
                }

                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur d'Or",
                        "Miner",
                        "minerais d'or"
                    )
                }
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de diamant",
                        "Miner",
                        "minerais de diamant"
                    )
                }

                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de redstone",
                        "Miner",
                        "minerais de redstone"
                    )
                }

                else -> Unit
            }
        }
    }

    private fun increasePlayerLevel(player: Player, playerData: PlayerBean) {
        while (playerData.currentXp >= getXpOfLevel(playerData.level)) {
            playerData.currentXp -= getXpOfLevel(playerData.level)
            playerData.level += 1
            playerData.skillPoint += 1

            playerLevelUpNotification(player, playerData)
            player.giveExpLevels(1)
        }
    }

    private fun getPlayerName(playerData: PlayerBean): String{
        playerData.uuid?.let {
            val player = Bukkit.getPlayer(UUID.fromString(playerData.uuid))
            player?.let { return player.name }
        }
        return "Unknown Player"
    }
    private fun handlePlayerScoreboard(){
        val top3 = session.createQuery("From PlayerBean p ORDER BY p.level desc", PlayerBean::class.java).setMaxResults(3).list()
        val scoreboard = Component.text(top3.withIndex().joinToString("\n") { "${it.index + 1} : " + getPlayerName(it.value) + " (${it.value.level})"})
        Bukkit.broadcast(Component.text("${ChatColor.GREEN}TOP 3").appendNewline().append(scoreboard))
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

        object: BukkitRunnable() {
            override fun run(){
                setPlayerVisualLevel(event.player, playerData)
                event.player.totalExperience = 0
                event.player.giveExpLevels(playerData.level)
            }
        }.runTaskLater(plugin, 2)
    }

}