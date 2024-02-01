package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.gameplay.nation.beans.NationBean
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Location
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

    var plotY: Int? = null

    constructor(linkedWorld: String) : this() {
        this.plotName = linkedWorld
    }

    fun getSeed(): Int {
        return this.plotName.hashCode()
    }

    fun showInfo(): Component {
        return plotRules.showWorldRules()
    }


    companion object {

        private const val RESERVED_PLOT_SIZE: Int = 4096
        private fun locationToPlotLocation(location: Location): Pair<Int, Int> {
            return Pair(location.blockX / RESERVED_PLOT_SIZE, location.blockZ / RESERVED_PLOT_SIZE)
        }

        private var plotsState: MutableMap<Pair<Int, Int>, PlotState> = mutableMapOf()

        fun getPlotState(location: Location): PlotState? {
            return plotsState[locationToPlotLocation(location)]
        }

        fun getPlotRules(location: Location): PlotRules? {
            return plotsState[locationToPlotLocation(location)]?.plotRules
        }

        private const val DEFAULT_UNASSIGNED_NAME = ""


        fun fetchPlotsState() {
            plotsState = PluginConfiguration.session
                .createQuery("from PlotState", PlotState::class.java)
                .list()
                .filter { it.plotX != null && it.plotY != null }
                .associateBy { Pair(it.plotX!!, it.plotY!!) }.toMutableMap()
        }

        private val lastGeneratedPosition: Pair<Int, Int>? = null

        /* The amount of space is finite, but we will run in other troubles before this one */
        private fun generateFreeCoordinates(): Pair<Int, Int> {
            // For now, simple stupid

            var generated = Pair(Random.nextInt(), Random.nextInt())
            while (generated in plotsState.keys) {
                generated = Pair(Random.nextInt(), Random.nextInt())
            }
            return generated
        }

        fun registerNewPlot(plotName: String, generalDifficultyFactor: Float) {
            // Resolve free Plot coordinates
            plotsState[generateFreeCoordinates()] = newPersistentWorldState(plotName, generalDifficultyFactor)
        }

        private fun newPersistentWorldState(linkedWorld: String, generalDifficultyFactor: Float): PlotState {
            PluginConfiguration.session.beginTransaction()
            val newPlotState = PlotState(linkedWorld)

            newPlotState.plotRules =
                PlotRules.generatePseudoRandomRules(newPlotState.getSeed(), generalDifficultyFactor)

            PluginConfiguration.session.persist(newPlotState)
            PluginConfiguration.session.transaction.commit()
            return newPlotState
        }
    }


}