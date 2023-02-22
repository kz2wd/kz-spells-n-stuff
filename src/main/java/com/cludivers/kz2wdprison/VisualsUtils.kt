package com.cludivers.kz2wdprison

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor

object VisualsUtils {

    fun progressBar(size: Int, progress: Float, progressColor: ChatColor, neutralColor: ChatColor): Component{
        val bar = "N".repeat(size)
        val scaledProgress = (size * progress).toInt()
        bar.replaceRange(scaledProgress, scaledProgress, neutralColor.toString())
        return Component.text("$progressColor${bar}")

    }

}