package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import org.bukkit.inventory.ItemStack

enum class Converters {
    ENTITIES_POSITION {
        override val customData: Int
            get() = 6000
    },
    SHIFT_POSITIONS {
        override val customData: Int
            get() = 6001
    },
    POSITIONS_AROUND {
        override val customData: Int
            get() = 6002
    },
    ENTITIES_AROUND {
        override val customData: Int
            get() = 6003
    },
    NONE {
        override val customData: Int
            get() = 5999
    };

    companion object {

        private val map = Converters.values().associateBy(Converters::customData)
        fun getConverter(itemStack: ItemStack): Converters {
            return map[itemStack.itemMeta.customModelData] ?: Converters.NONE
        }
    }
    abstract val customData: Int
}