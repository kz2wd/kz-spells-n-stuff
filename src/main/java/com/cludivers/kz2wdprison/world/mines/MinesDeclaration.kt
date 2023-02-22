package com.cludivers.kz2wdprison.world.mines

import com.cludivers.kz2wdprison.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.commands.mine.MineListCommand
import com.cludivers.kz2wdprison.commands.mine.MineResetCommand
import com.cludivers.kz2wdprison.world.cuboid.BlockPattern
import com.cludivers.kz2wdprison.world.cuboid.Cuboid
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

object MinesDeclaration {
    fun declare(plugin: JavaPlugin){

        val mines = listOf(
            PrisonMine("StarterMine",
                Cuboid(
                    Location(Bukkit.getWorld("world"), -10.0, 100.0, -10.0),
                    Location(Bukkit.getWorld("world"), 10.0, 120.0, 10.0)
                ),
                BlockPattern(listOf(Pair(Material.STONE, 1f),
                    Pair(Material.COAL_ORE, .15f),
                    Pair(Material.IRON_ORE, .1f))),
                Bukkit.getWorld("world")!!.spawnLocation,
                MineHandler.minuteToTick(10)
            )
        )

        val mineHandler = MineHandler(MineHandler.minuteToTick(1), plugin, mines)
        val mineCommandName = "mine"
        val listMineCmd = MineListCommand(mineHandler, mineCommandName)
        val mineCommandsExecutor = MainCommandExecutor(
            mapOf("list" to listMineCmd, "reset" to MineResetCommand(mineHandler, mineCommandName)), listMineCmd)

        plugin.getCommand(mineCommandName)?.setExecutor(mineCommandsExecutor)
        plugin.getCommand(mineCommandName)?.tabCompleter = mineCommandsExecutor

    }
}