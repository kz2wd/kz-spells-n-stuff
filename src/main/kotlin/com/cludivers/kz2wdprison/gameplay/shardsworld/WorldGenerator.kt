package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.fastasyncworldedit.core.util.TaskManager
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
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


        generatePlotTerrain(createdPlot)


    }

    fun generateShape(region: Region, editSession: EditSession, pattern: Pattern, expression: String, hollow: Boolean) {
        // Made with the help of :
        // https://github.com/EngineHub/WorldEdit/blob/master/worldedit-core/src/main/java/com/sk89q/worldedit/command/GenerationCommands.java#L362
        val min = region.minimumPoint.toVector3()
        val max = region.maximumPoint.toVector3()
        val origin = max.add(min).divide(2.0)
        val unit = max.subtract(min)
        TaskManager.taskManager().async {
            editSession.makeShape(region, origin, unit, pattern, expression, hollow)
            editSession.flushQueue()
        }
    }

    fun generatePlotTerrain(plot: PlotState) {

        val generationArea = CuboidRegion.fromCenter(plot.getCuboidRegion().center.toBlockPoint(), 100)
        generateShape(
            generationArea,
            WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(Bukkit.getWorld("world"))),
            BlockTypes.MOSS_BLOCK!!, "((1/5)*(x^2+y^2+z^2))^-6+((2)^-8*(x^8+y^8+z^8))^6-1", false
        )

    }

    fun generateSphere(center: Location, size: Double, block: BlockType, world: World) {
        generateSphere(BukkitAdapter.asBlockVector(center), size, block, world)
    }

    fun generateSphere(center: BlockVector3, size: Double, block: BlockType, world: World) {
        TaskManager.taskManager().async {
            val editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))

            val sphereRegion = SphereRegionFactory().createCenteredAt(center, size)
            editSession.setBlocks(sphereRegion, block)


            // Flush the edit session to apply changes
            editSession.flushQueue()
        }
    }
}

