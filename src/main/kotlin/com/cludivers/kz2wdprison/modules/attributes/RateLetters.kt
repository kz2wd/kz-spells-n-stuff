package com.cludivers.kz2wdprison.modules.attributes

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor

enum class RateLetters {

    DIVINE {
        override val threshold: Float
            get() = .9f
        override val text: Component
            get() = Component.text("DIVINE").color(NamedTextColor.LIGHT_PURPLE).decorate(
                TextDecoration.BOLD
            )
    },
    MYTHIC {
        override val threshold: Float
            get() = .8f
        override val text: Component
            get() = Component.text("Mythic").color(NamedTextColor.RED).decorate()
    },
    SSS {
        override val threshold: Float
            get() = .7f
        override val text: Component
            get() = Component.text("SSS").color(NamedTextColor.GOLD).decorate()
    },
    SS {
        override val threshold: Float
            get() = .6f
        override val text: Component
            get() = Component.text("SS").color(NamedTextColor.GOLD).decorate()
    },
    S {
        override val threshold: Float
            get() = .5f
        override val text: Component
            get() = Component.text("S").color(NamedTextColor.GOLD).decorate()
    },
    A {
        override val threshold: Float
            get() = .4f
        override val text: Component
            get() = Component.text("A").color(NamedTextColor.GREEN).decorate()
    },
    B {
        override val threshold: Float
            get() = .3f
        override val text: Component
            get() = Component.text("B").color(NamedTextColor.WHITE).decorate()
    },
    C {
        override val threshold: Float
            get() = .2f
        override val text: Component
            get() = Component.text("C").color(NamedTextColor.DARK_GRAY).decorate()
    },
    D {
        override val threshold: Float
            get() = .1f
        override val text: Component
            get() = Component.text("D").color(NamedTextColor.GRAY).decorate()
    },
    E {
        override val threshold: Float
            get() = .0f
        override val text: Component
            get() = Component.text("E").color(NamedTextColor.GRAY).decorate()
    };

    abstract val threshold: Float
    abstract val text: Component

    companion object {

        private val unknownRankComponent = Component.text("unknown").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.OBFUSCATED)

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