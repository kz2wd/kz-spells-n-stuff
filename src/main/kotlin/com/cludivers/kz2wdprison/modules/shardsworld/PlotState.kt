package com.cludivers.kz2wdprison.modules.shardsworld

import com.cludivers.kz2wdprison.Kz2wdPrison.Companion.MAIN_WORLD
import com.cludivers.kz2wdprison.framework.configuration.FetchAfterDatabaseInit
import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.framework.utils.Utils.debug
import com.cludivers.kz2wdprison.modules.nation.beans.NationBean
import com.cludivers.kz2wdprison.modules.shardsworld.rules.PlotRules
import com.cludivers.kz2wdprison.modules.shardsworld.rules.RulesBoolean
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.random.Random

@Entity
class PlotState() {

    @Id
    @GeneratedValue
    var id: Long? = null

    var plotName: String = DEFAULT_UNASSIGNED_NAME

    @Embedded
    var plotRules: PlotRules = PlotRules()

    var overallShardsValue: Int = 0

    var worldLevel: Int = 0

    @ManyToOne
    var owningNation: NationBean? = null

    var plotX: Int? = null

    var plotZ: Int? = null

    var spawnOffsetX: Int = RESERVED_PLOT_SIZE / 2
    var spawnOffsetY: Int = 128
    var spawnOffsetZ: Int = RESERVED_PLOT_SIZE / 2

    var shards: Double = Random.nextDouble(100000.0)

    constructor(linkedWorld: String) : this() {
        this.plotName = linkedWorld
    }

    fun getSeed(): Int {
        return this.plotName.hashCode()
    }

    fun showInfo(): Component {
        return plotRules.showWorldRules()
    }

    fun worldPlotCoordinates(world: World, y: Double): Location? {
        if (plotX == null || plotZ == null) return null
        val worldLoc = plotLocationToWorldLocation(Pair(plotX!!, plotZ!!))
        return Location(world, worldLoc.first.toDouble(), y, worldLoc.second.toDouble())
    }

    fun worldPlotCenterCoordinates(world: World, y: Double): Location? {
        val loc = worldPlotCoordinates(world, y) ?: return null
        loc.add(Vector(RESERVED_PLOT_SIZE / 2, 0, RESERVED_PLOT_SIZE / 2))
        return loc
    }

    fun getCuboidRegion(): CuboidRegion {
        val flatCoord = plotLocationToWorldLocation(Pair(plotX!!, plotZ!!))
        return CuboidRegion(
            BlockVector3.at(flatCoord.first, -64, flatCoord.second),
            BlockVector3.at(flatCoord.first + RESERVED_PLOT_SIZE, 320, flatCoord.second + RESERVED_PLOT_SIZE)
        )

    }

    fun getSpawnLocation(world: World): Location? {
        if (plotX == null || plotZ == null) return null
        val inWorld = plotLocationToWorldLocation(Pair(plotX!!, plotZ!!))
        return Location(
            world, (inWorld.first + spawnOffsetX).toDouble(), spawnOffsetY.toDouble(),
            (inWorld.second + spawnOffsetZ).toDouble()
        )
    }

    fun takeShards(requestAmount: Double): Double {
        val shardsTaken = if (shards < requestAmount) max(shards, requestAmount) else requestAmount
        shards -= shardsTaken
        return shardsTaken
    }

    companion object {

        private const val RESERVED_PLOT_SIZE: Int = 4096

        private fun plotLocationToWorldLocation(location: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(location.first * RESERVED_PLOT_SIZE, location.second * RESERVED_PLOT_SIZE)
        }

        fun worldLocationToPlotLocation(location: Location): Pair<Int, Int> {
            return worldLocationToPlotLocation(location.blockX, location.blockZ)
        }

        private fun worldLocationToPlotLocation(x: Int, z: Int): Pair<Int, Int> {
            val posX =
                if (x < 0) x / RESERVED_PLOT_SIZE - 1 else x / RESERVED_PLOT_SIZE
            val posZ =
                if (z < 0) z / RESERVED_PLOT_SIZE - 1 else z / RESERVED_PLOT_SIZE
            return Pair(posX, posZ)
        }

        private lateinit var plotsState: MutableMap<Pair<Int, Int>, PlotState>

        fun getPlotState(location: Location): PlotState? {
            return plotsState[worldLocationToPlotLocation(location)]
        }

        fun getPlotState(x: Int, z: Int): PlotState? {
            return plotsState[worldLocationToPlotLocation(x, z)]
        }

        fun getPlotFromPlotLocation(plotX: Int, plotZ: Int): PlotState? {
            return plotsState[Pair(plotX, plotZ)]
        }

        fun getPlotRules(location: Location): PlotRules? {
            return plotsState[worldLocationToPlotLocation(location)]?.plotRules
        }

        fun getAllPLots(): List<PlotState> {
            return plotsState.values.toList()
        }

        fun plotFromName(name: String): PlotState? {
            return plotsState.values.firstOrNull { it.plotName == name }
        }

        private const val DEFAULT_UNASSIGNED_NAME = ""

        @FetchAfterDatabaseInit
        private fun fetchPlotsState() {
            plotsState = PluginConfiguration.session
                .createQuery("from PlotState", PlotState::class.java)
                .list()
                .filter { it.plotX != null && it.plotZ != null }
                .associateBy { Pair(it.plotX!!, it.plotZ!!) }.toMutableMap()

            // Set spawn here once plotState is init
            // I feel like using Delegates could be a better way than the @FetchAfterDatabaseInit annotation
            setSpawn()
        }

        private val lastGeneratedPosition: Pair<Int, Int>? = null

        /* The amount of space is finite, but we will run in other troubles before this one */
        private fun generateFreeCoordinates(): Pair<Int, Int> {
            // For now, simple stupid

            var generated = Pair(Random.nextInt(-100, 100), Random.nextInt(-100, 100))
            while (generated in plotsState.keys) {
                generated = Pair(Random.nextInt(), Random.nextInt())
            }
            return generated
        }

        fun registerNewPlot(plotName: String, generalDifficultyFactor: Float): PlotState {
            // Resolve free Plot coordinates
            val freeCoord = generateFreeCoordinates()
            return registerNewPlot(plotName, freeCoord, generalDifficultyFactor)
        }

        fun registerNewPlot(plotName: String, coordinates: Pair<Int, Int>, generalDifficultyFactor: Float): PlotState {
            val plotState = newPersistentPlotState(plotName, coordinates, generalDifficultyFactor)
            plotsState[coordinates] = plotState
            debug("Adding plotstate at coord $coordinates")
            return plotState
        }

        private fun newPersistentPlotState(
            plotName: String,
            coordinates: Pair<Int, Int>,
            generalDifficultyFactor: Float
        ): PlotState {
            PluginConfiguration.session.beginTransaction()
            val newPlotState = PlotState(plotName)
            newPlotState.plotX = coordinates.first
            newPlotState.plotZ = coordinates.second

            newPlotState.plotRules =
                PlotRules.generatePseudoRandomRules(newPlotState.getSeed(), generalDifficultyFactor)

            PluginConfiguration.session.persist(newPlotState)
            PluginConfiguration.session.transaction.commit()
            return newPlotState
        }

        lateinit var SPAWN_PLOT: PlotState

        fun setSpawn() {
            SPAWN_PLOT = getPlotState(0, 0) ?: registerNewPlot("Spawn", Pair(0, 0), 0.0f)
            SPAWN_PLOT.plotRules.rulesBoolean[RulesBoolean.ALLOW_PVP] = false
            SPAWN_PLOT.plotRules.rulesBoolean[RulesBoolean.MOB_SPAWNING] = false
            SPAWN_PLOT.plotRules.rulesBoolean[RulesBoolean.MOB_GRIEFING] = false
            SPAWN_PLOT.plotRules.rulesBoolean[RulesBoolean.ALLOW_MODIFICATION] = false
            SPAWN_PLOT.getSpawnLocation(MAIN_WORLD)
        }
    }


}