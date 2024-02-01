package com.cludivers.kz2wdprison.gameplay.shardsworld

import org.bukkit.plugin.java.JavaPlugin

object WorldGenerator {

    lateinit var plugin: JavaPlugin
    fun generateNewPlot(plotName: String) {
        if (plotName.isBlank()) {
            throw Exception("Blank name cannot be accepted")
        }

        PlotState.registerNewPlot(plotName, 1.0f)

    }
}