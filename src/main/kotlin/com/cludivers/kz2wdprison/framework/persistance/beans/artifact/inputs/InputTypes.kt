package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

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
    EMPTY_LOCATION_SIGHT {
        override val customData: Int
            get() = 5004
    },
    NONE {
        override val customData: Int
            get() = 4999
    };

    companion object {

        private val map = InputTypes.values().associateBy(InputTypes::customData)
        fun getInputType(itemStack: ItemStack): InputTypes {
            return if (itemStack.itemMeta.hasCustomModelData()){
                map[itemStack.itemMeta.customModelData] ?: NONE
            } else {
                NONE
            }
        }
    }
    abstract val customData: Int

}