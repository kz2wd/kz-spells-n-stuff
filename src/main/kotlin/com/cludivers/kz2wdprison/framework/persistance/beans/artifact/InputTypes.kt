package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

enum class InputTypes {
    ENTITY_CASTER {
        override val customData: Int
            get() = 5000
    },
    ENTITY_SIGHT {
        override val customData: Int
            get() = 5001
    },
    ENTITY_BOUND {
        override val customData: Int
            get() = 5002
    },
    LOCATION_SIGHT {
        override val customData: Int
            get() = 5003
    },
    NONE {
        override val customData: Int
            get() = 4999
    };

    companion object {

        private val map = InputTypes.values().associateBy(InputTypes::customData)
        fun getInputType(itemStack: ItemStack): InputTypes {
            return map[itemStack.itemMeta.customModelData] ?: NONE
        }
    }
    abstract val customData: Int

}