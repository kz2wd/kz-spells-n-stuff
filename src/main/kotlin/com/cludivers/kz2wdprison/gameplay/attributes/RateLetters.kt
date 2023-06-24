package com.cludivers.kz2wdprison.gameplay.attributes

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor

enum class RateLetters {

    DIVIN {
        override val threshold: Float
            get() = .9f
        override val text: Component
            get() = Component.text("${ChatColor.BOLD}${ChatColor.LIGHT_PURPLE}DIVIN")
    },
    MYTHIQUE {
        override val threshold: Float
            get() = .8f
        override val text: Component
            get() = Component.text("${ChatColor.RED}Mythique")
    },
    SSS {
        override val threshold: Float
            get() = .7f
        override val text: Component
            get() = Component.text("${ChatColor.GOLD}SSS")
    },
    SS {
        override val threshold: Float
            get() = .6f
        override val text: Component
            get() = Component.text("${ChatColor.GOLD}SS")
    },
    S {
        override val threshold: Float
            get() = .5f
        override val text: Component
            get() = Component.text("${ChatColor.GOLD}S")
    },
    A {
        override val threshold: Float
            get() = .4f
        override val text: Component
            get() = Component.text("${ChatColor.GREEN}A")
    },
    B {
        override val threshold: Float
            get() = .3f
        override val text: Component
            get() = Component.text("${ChatColor.WHITE}B")
    },
    C {
        override val threshold: Float
            get() = .2f
        override val text: Component
            get() = Component.text("${ChatColor.DARK_GRAY}C")
    },
    D {
        override val threshold: Float
            get() = .1f
        override val text: Component
            get() = Component.text("${ChatColor.GRAY}D")
    },
    E {
        override val threshold: Float
            get() = .0f
        override val text: Component
            get() = Component.text("${ChatColor.GRAY}E")
    };

    abstract val threshold: Float
    abstract val text: Component

    companion object {

        val unknownRankComponent = Component.text("${ChatColor.MAGIC}unknown")

        private val ratesToLetter = RateLetters.values().associateBy { it.threshold }.toSortedMap()
        private val rates = ratesToLetter.keys.toList()
        private val letters = ratesToLetter.values.toList()
        fun getAttributeRateLetter(ratio: Double): Component {
            val index = rates.indexOfFirst { it >= ratio }
            if (index == -1) return unknownRankComponent
            return letters[index].text

        }

    }

}