package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.gameplay.utils.Utils.debug
import com.fastasyncworldedit.core.util.TaskManager
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.block.BlockReplace
import com.sk89q.worldedit.function.block.Naturalizer
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.function.visitor.LayerVisitor
import com.sk89q.worldedit.function.visitor.RegionVisitor
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.regions.Regions
import com.sk89q.worldedit.regions.Regions.maximumBlockY
import com.sk89q.worldedit.regions.Regions.minimumBlockY
import com.sk89q.worldedit.regions.factory.SphereRegionFactory
import com.sk89q.worldedit.util.TreeGenerator
import com.sk89q.worldedit.world.block.BlockType
import com.sk89q.worldedit.world.block.BlockTypes
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random


object WorldGenerator {

    lateinit var plugin: JavaPlugin

    fun generateNewPlot(plotName: String, shape: GenerationShapes) {
        if (plotName.isBlank()) {
            throw Exception("Blank name cannot be accepted")
        }
        val createdPlot = PlotState.registerNewPlot(plotName, 1.0f)
        generatePlotTerrain(createdPlot, shape)
    }

    fun generateNewPlot(plotName: String, coordinates: Pair<Int, Int>, shape: GenerationShapes) {
        if (plotName.isBlank()) {
            throw Exception("Blank name cannot be accepted")
        }
        val createdPlot = PlotState.registerNewPlot(plotName, coordinates, 1.0f)
        generatePlotTerrain(createdPlot, shape)
    }

    fun runTaskManager(editSession: EditSession, tasks: List<(EditSession) -> Unit>) {
        TaskManager.taskManager().async {
            tasks.forEach {
                it(editSession)
            }

            editSession.flushQueue()
            debug("Generation tasks done")
        }
    }

    fun customNaturalizer(editSession: EditSession, region: Region) {

        val naturalizer = Naturalizer(editSession)
        val flatRegion = Regions.asFlatRegion(region)
        val visitor = LayerVisitor(flatRegion, minimumBlockY(region), maximumBlockY(region), naturalizer)
        Operations.completeLegacy(visitor)

    }

    fun generatePlotTerrain(plot: PlotState, shape: GenerationShapes) {

        val seed = Random.nextInt()
        val generationArea = CuboidRegion.fromCenter(plot.getCuboidRegion().center.toBlockPoint(), 100)

        val generationTasks: List<(EditSession) -> Unit> =
            listOf(
                { editSession ->
                    Operations.completeBlindly(RegionVisitor(generationArea, BlockReplace(editSession, BlockTypes.AIR)))
                },
                { editSession -> editSession.flushQueue() },
                shape.getGeneration(generationArea, BlockTypes.STONE!!, false, seed),
                { editSession -> editSession.flushQueue() },
                { editSession -> editSession.naturalizeCuboidBlocks(generationArea) },
                { editSession -> editSession.makeForest(generationArea, 0.005, TreeGenerator.TreeType.CHERRY) }
            )

        val editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(Bukkit.getWorld("world")))
        runTaskManager(editSession, generationTasks)
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

