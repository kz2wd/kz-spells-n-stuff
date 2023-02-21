package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.mines.MineHandler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

object BonusXpEvent: BukkitRunnable() {
    fun start(plugin: JavaPlugin) {

        val blocs = mapOf(
            Material.STONE to "pierre",
            Material.IRON_ORE to "minerais de fer",
            Material.COAL_ORE to "minerais de charbon"
        )

        blocsMaterial = blocs.keys.toList()
        blocsNames = blocs

        this.runTaskTimer(plugin, 0, MineHandler.minuteToTick(20).toLong())
    }
    private var blocsMaterial: List<Material>? = null
    private var blocsNames: Map<Material, String>? = null

    var currentBlockWithBonus: Material? = null
    var currentBonusFactor: Float = 1f

    private fun getEventMessage(): Component {
        return Component.text("${ChatColor.BLUE}[EVENEMENT] Bonus de ${ChatColor.BOLD}$currentBonusFactor en minant : " +
                "${blocsNames?.get(currentBlockWithBonus)}")
    }

    override fun run() {
        currentBlockWithBonus = blocsMaterial?.random()
        currentBonusFactor = Random.nextFloat() * 2 + 1
        Bukkit.broadcast(getEventMessage())
    }

    fun getBonusFactor(minedMaterial: Material): Float {
        if (minedMaterial == currentBlockWithBonus){
            return currentBonusFactor
        }
        return 1f
    }

}