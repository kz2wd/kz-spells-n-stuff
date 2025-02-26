package com.cludivers.kz2wdprison.modules.player.visuals

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

object VisualsUtils {

    fun progressBar(size: Int, progress: Float, progressColor: TextColor, neutralColor: TextColor): Component{
        val bar = "N".repeat(size)
        val scaledProgress = (size * progress).toInt()
        bar.replaceRange(scaledProgress, scaledProgress, neutralColor.toString())
        return Component.text("$progressColor${bar}")

    }

}