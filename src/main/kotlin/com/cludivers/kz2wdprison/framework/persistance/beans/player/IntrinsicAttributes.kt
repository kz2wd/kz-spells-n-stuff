package com.cludivers.kz2wdprison.framework.persistance.beans.player

import org.bukkit.attribute.Attribute

enum class IntrinsicAttributes {
    VIGOR {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_MAX_HEALTH
    },
    AGILITY {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_MOVEMENT_SPEED

        override fun intrinsicToGenericValue(value: Double): Double {
            return value * 0.1
        }
    },
    STRENGTH {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_ATTACK_DAMAGE
    },
    TOUGHNESS {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_ARMOR

        override val baseValue = 0
    },
    DEXTERITY {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_ATTACK_SPEED

        override val baseValue = 4

    };

    abstract val relatedAttribute: Attribute

    open val baseValue = 1

    fun intrinsicToGenericValue(value: Int): Double {
        return intrinsicToGenericValue(value.toDouble())
    }

    /**
     * WARNING : This function MUST be linear
     * ensure that it respects f(a) + f(b) = f(a + b)
     */
    open fun intrinsicToGenericValue(value: Double): Double {
        return value
    }


}