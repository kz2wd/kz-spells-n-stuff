package com.cludivers.kz2wdprison.listeners

import com.cludivers.kz2wdprison.BonusXpEvent
import com.cludivers.kz2wdprison.beans.PlayerBean
import com.cludivers.kz2wdprison.beans.ores.OreStats
import com.cludivers.kz2wdprison.beans.ores.Ores
import com.cludivers.kz2wdprison.bossBarDisplay
import com.cludivers.kz2wdprison.getData
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

class PrisonListener(private val plugin: JavaPlugin, private val session: Session): Listener {

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

        private fun getInRange(min: Float, max: Float): Pair<Float, Float>{
            val scale = Random.nextFloat()
            return Pair(min + scale * (max - min), scale)
        }

        private fun getBlockValue(block: Material, playerData: PlayerBean): Pair<Float, Float>{
            val bonus = BonusXpEvent.getBonusFactor(block)
            return when(block) {
                Material.STONE, Material.ANDESITE, Material.GRANITE, Material.TUFF, Material.DIORITE ->
                    getInRange(Ores.STONE.minXp, playerData.oresStats.stone.maxXp * bonus)
                Material.COAL_ORE -> getInRange(Ores.COAL.minXp, playerData.oresStats.coal.maxXp * bonus)
                Material.IRON_ORE, Material.COPPER_ORE -> getInRange(Ores.IRON.minXp, playerData.oresStats.iron.maxXp * bonus)
                Material.GOLD_ORE -> getInRange(Ores.GOLD.minXp, playerData.oresStats.gold.maxXp * bonus)
                Material.REDSTONE_ORE, Material.LAPIS_ORE -> getInRange(Ores.REDSTONE.minXp, playerData.oresStats.redstone.maxXp * bonus)
                Material.DIAMOND_ORE, Material.EMERALD_ORE -> getInRange(Ores.DIAMOND.minXp, playerData.oresStats.diamond.maxXp * bonus)
                Material.DEEPSLATE -> getInRange(Ores.STONE.minXp, (playerData.oresStats.stone.maxXp + 5)  * bonus)
                Material.DEEPSLATE_COAL_ORE -> getInRange(Ores.COAL.minXp, (playerData.oresStats.coal.maxXp + 5)  * bonus)
                Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_COPPER_ORE -> getInRange(Ores.IRON.minXp, (playerData.oresStats.iron.maxXp + 5)  * bonus)
                Material.DEEPSLATE_GOLD_ORE -> getInRange(Ores.GOLD.minXp, (playerData.oresStats.gold.maxXp + 5) * bonus)
                Material.DEEPSLATE_REDSTONE_ORE, Material.DEEPSLATE_LAPIS_ORE -> getInRange(Ores.REDSTONE.minXp, (playerData.oresStats.redstone.maxXp + 5) * bonus)
                Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE -> getInRange(Ores.DIAMOND.minXp, (playerData.oresStats.diamond.maxXp)  * bonus)
                else -> getInRange(0f, 1f)
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

        fun updatePlayerLevelDisplay(player: Player, playerData: PlayerBean){
            val playerNameDisplay = Component.text("${ChatColor.GREEN}LVL ${playerData.level} ${ChatColor.RESET}${player.name}")
            player.displayName(playerNameDisplay)
            player.playerListName(playerNameDisplay)
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

    private fun playerLevelUpMessage(playerData: PlayerBean): Pair<Component, Component> {
        val main = Component.text("Niveau ${playerData.level} atteint !").color(TextColor.color(165425554))
        val exp = Component.text(
            "${ChatColor.GREEN}Vous avez ${ChatColor.GOLD}${playerData.skillPoint}" +
                    "${ChatColor.GREEN} point${if (playerData.skillPoint > 1) "s" else ""} de compétence."
        )

        return Pair(main, exp)
    }
    private fun playerLevelUpNotification(player: Player, playerData: PlayerBean){
        val txt = playerLevelUpMessage(playerData)
        player.sendMessage((txt.first).appendNewline().append(txt.second))
        player.world.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, .7f, .8f)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        val transaction = session.beginTransaction()
        val playerData = event.player.getData(session)
        if (playerData.connectionAmount < 1) {
            Bukkit.broadcast(Component.text("Bienvenue à ${ChatColor.LIGHT_PURPLE}${event.player.name} ${ChatColor.WHITE} !"))
        } else {
            event.player.sendMessage(Component.text("Rebonjour ${ChatColor.LIGHT_PURPLE}${event.player.name}"))
        }
        playerData.connectionAmount += 1
        updatePlayerStats(event.player, playerData)
        updatePlayerPickaxe(event.player, playerData, false)
        event.player.sendMessage(BonusXpEvent.getEventMessage())
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

        val playerData = event.player.getData(session)

        var (xpGained, scale) = getBlockValue(event.block.type, playerData)
        var colorEffect = getColorEffect(scale)
        var criticMsg = ""
        // Try to do a critic Hit
        if (Random.nextFloat() < getCriticOdd(playerData.criticOddLevel)){
            // Try to do a 2nd crit
            if (Random.nextFloat() < .1f){
                criticMsg = "SUPER CRITIQUE "
                colorEffect = "${ChatColor.BOLD}${ChatColor.DARK_PURPLE}"
                xpGained *= getCriticFactor(playerData.criticFactorLevel) * 2
                event.player.world.playSound(event.player, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, .0f)
            } else {
                criticMsg = "Critique "
                colorEffect = "${ChatColor.LIGHT_PURPLE}"
                xpGained *= getCriticFactor(playerData.criticFactorLevel)
                event.player.world.playSound(event.player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, .0f)
            }
        }

        event.player.sendActionBar(Component.text("$colorEffect ${criticMsg}+ ${"%.1f".format(xpGained)}"))
        playerData.currentXp += xpGained

        increasePlayerLevel(event.player, playerData)
        checkAchievements(event.player, playerData, event.block.type)
        setPlayerVisualLevel(event.player, playerData)
        transaction.commit()
    }


    private fun applyAchievement(player: Player, stats: OreStats, amount: List<Int>, name: String, action: String, materials: String, color: ChatColor) {
        val base = Component.text("${ChatColor.GOLD}${ChatColor.BOLD}SUCCES OBTENU ")
        val indexToChar = listOf("I", "II", "II", "IV", "V", "VI", "VII", "VIII", "IX", "X")

        val currAchievementLevel = amount.binarySearch { it - stats.amountMined }
        if (currAchievementLevel >= amount.size){
            return
        }

        if (currAchievementLevel >= 0){
            // Player just got achievement, award him

            val achiev = Component.text("$name ${indexToChar[currAchievementLevel]}")
            val hover = hoverEvent(HoverEvent.Action.SHOW_TEXT,  Component.text("$action ${amount[currAchievementLevel]} $materials"))
            player.sendMessage(base.append(achiev).hoverEvent(hover))
            val award = Component.text("${ChatColor.GREEN}Récompense : Xp obtenu en minant des ${ChatColor.RESET}$materials ${ChatColor.BOLD}augmenté !")
            val awardHover = hoverEvent(HoverEvent.Action.SHOW_TEXT,  Component.text("XP max : ${stats.maxXp} => ${stats.maxXp * 2}"))

            player.sendMessage(award.hoverEvent(awardHover))
            player.world.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, .0f)
            stats.maxXp *= 1.2f

        } else {
            // Player has made progress toward getting achievement, tells him
            val progress: Float = stats.amountMined.toFloat() / amount[-currAchievementLevel - 1]
            player.bossBarDisplay(Component.text("${color}Prochain succès : " +
                    "$action ${amount[-currAchievementLevel - 1]} $materials"), progress)
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
                        "pierres ou pierres des abîmes",
                        ChatColor.GRAY
                    )
                }
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Charbon",
                        "Miner",
                        "minerais de charbon",
                        ChatColor.DARK_GRAY
                    )
                }

                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Métaux",
                        "Miner",
                        "minerais de fer ou minerais de cuivre",
                        ChatColor.GOLD
                    )
                }

                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur d'Or",
                        "Miner",
                        "minerais d'or",
                        ChatColor.YELLOW
                    )
                }
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Pierres précieuses",
                        "Miner",
                        "minerais de diamant ou minerais d'émeraude",
                        ChatColor.AQUA
                    )
                }

                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> {
                    applyAchievement(
                        player,
                        it,
                        listOf(10, 100, 500, 1000, 2000, 5000, 10000, 25000, 50000, 100000, 200000),
                        "Mineur de Pierre rares",
                        "Miner",
                        "minerais de redstone ou minerais de lapis",
                        ChatColor.RED
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
            updatePlayerLevelDisplay(player, playerData)
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
        val playerData = event.player.getData(session)
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