package com.cludivers.kz2wdprison.modules.artifact

import org.bukkit.event.block.Action

enum class ArtifactTriggers {
    NONE,
    RIGHT_CLICK_AIR,
    LEFT_CLICK_AIR,
    RIGHT_CLICK_BLOCK,
    LEFT_CLICK_BLOCK,
    RIGHT_CLICK {
        override val extraAcceptedTriggers = setOf(RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK)
    },
    LEFT_CLICK {
        override val extraAcceptedTriggers = setOf(LEFT_CLICK_AIR, LEFT_CLICK_BLOCK)
    },
    CLICK {
        override val extraAcceptedTriggers = RIGHT_CLICK.extraAcceptedTriggers + LEFT_CLICK.extraAcceptedTriggers
    },
    ATTACKED,
    ATTACKING;

    open val extraAcceptedTriggers: Set<ArtifactTriggers> = setOf()

    fun matchTrigger(trigger: ArtifactTriggers): Boolean {
        return this == trigger || this.extraAcceptedTriggers.contains(trigger)
    }

    companion object {
        fun getClickType(action: Action): ArtifactTriggers {
            return when (action){
                Action.RIGHT_CLICK_AIR -> RIGHT_CLICK_AIR
                Action.RIGHT_CLICK_BLOCK -> RIGHT_CLICK_BLOCK
                Action.LEFT_CLICK_AIR -> LEFT_CLICK_AIR
                Action.LEFT_CLICK_BLOCK -> LEFT_CLICK_BLOCK
                else -> NONE
            }
        }
    }
}
