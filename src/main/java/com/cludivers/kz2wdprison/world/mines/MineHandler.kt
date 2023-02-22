package com.cludivers.kz2wdprison.world.mines

import com.cludivers.kz2wdprison.world.cuboid.BlockPattern
import com.cludivers.kz2wdprison.world.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class MineHandler(private val tickPerUpdate: Int, plugin: JavaPlugin, val allMines: List<PrisonMine>): BukkitRunnable() {

    init {
        runTaskTimer(plugin, 0, tickPerUpdate.toLong())
    }

    companion object {
        fun minuteToTick(minutes: Int): Int{
            return minutes * 60 * 20
        }
    }

    val nameToMine = allMines.associateBy { it.name }
    private val minesWithTimer = allMines.associateWith { 0 } as MutableMap<PrisonMine, Int>

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