package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.fastasyncworldedit.core.util.TaskManager
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.factory.SphereRegionFactory
import com.sk89q.worldedit.world.block.BlockType
import com.sk89q.worldedit.world.block.BlockTypes
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin

object WorldGenerator {

    lateinit var plugin: JavaPlugin
    fun generateNewPlot(plotName: String) {
        if (plotName.isBlank()) {
            throw Exception("Blank name cannot be accepted")
        }

        val createdPlot = PlotState.registerNewPlot(plotName, 1.0f)


        generateCylinder(
            createdPlot.getCuboidRegion().center.toBlockPoint(),
            10.0,
            BlockTypes.MOSS_BLOCK!!,
            Bukkit.getWorld("world")!!
        )

    }

    fun generateCylinder(center: Location, size: Double, block: BlockType, world: World) {
        generateCylinder(BukkitAdapter.asBlockVector(center), size, block, world)
    }

    fun generateCylinder(center: BlockVector3, size: Double, block: BlockType, world: World) {
        TaskManager.taskManager().async {
            val editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))

            val sphereRegion = SphereRegionFactory().createCenteredAt(center, size)
            editSession.setBlocks(sphereRegion, block)

            // Flush the edit session to apply changes
            editSession.flushQueue()
        }
    }
}

