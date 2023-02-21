package com.cludivers.kz2wdprison.mines

import com.cludivers.kz2wdprison.cuboid.BlockPattern
import com.cludivers.kz2wdprison.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class MineHandler(private val tickPerUpdate: Int, plugin: JavaPlugin): BukkitRunnable() {

    init {
        runTaskTimerAsynchronously(plugin, 0, tickPerUpdate.toLong())
    }

    companion object {
        fun minuteToTick(minutes: Int): Int{
            return minutes * 60 * 20
        }
    }

    private fun allMines(): List<PrisonMine>{
        return listOf(
            PrisonMine(
                Cuboid(
                    Location(Bukkit.getWorld("world"), -10.0, 100.0, -10.0),
                    Location(Bukkit.getWorld("world"), 10.0, 120.0, 10.0)),
                BlockPattern(listOf(Pair(Material.STONE, 1f),
                                    Pair(Material.COAL_ORE, .15f),
                                    Pair(Material.IRON_ORE, .1f))),
                Bukkit.getWorld("world")!!.spawnLocation,
                minuteToTick(10)
            )
        )
    }

    private val minesWithTimer = allMines().associateWith { 0 } as MutableMap<PrisonMine, Int>

    override fun run() {
        minesWithTimer.forEach {
            if (it.value >= it.key.respawnTimeInTick){
                it.key.reset()
                minesWithTimer[it.key] = 0
            } else {
                minesWithTimer[it.key] = minesWithTimer[it.key]!! + tickPerUpdate
            }

        }
    }
}