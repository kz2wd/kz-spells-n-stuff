package com.cludivers.kz2wdprison.modules.attributes

import org.bukkit.attribute.Attribute

enum class IntrinsicAttributes {
    VIGOR {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_MAX_HEALTH
        override val text: String
            get() = "Vigueur"

        override fun intrinsicToGenericValue(value: Double): Double {
            return value
        }
    },
    AGILITY {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_MOVEMENT_SPEED
        override val text: String
            get() = "Agilité"

        override fun intrinsicToGenericValue(value: Double): Double {
            return value * 0.001
        }
    },
    STRENGTH {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_ATTACK_DAMAGE
        override val text: String
            get() = "Force"
    },
    TOUGHNESS {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_ARMOR

        override val baseValue = 0
        override val text: String
            get() = "Résistance"
    },
    DEXTERITY {
        override val relatedAttribute: Attribute
            get() = Attribute.GENERIC_ATTACK_SPEED

        override val baseValue = 4
        override val text: String
            get() = "Dextérité"
    };

    abstract val relatedAttribute: Attribute

    open val baseValue = 1

    abstract val text: String

    fun intrinsicToGenericValue(value: Int): Double {
        return intrinsicToGenericValue(value.toDouble())
    }

    open fun intrinsicToGenericValue(value: Double): Double {
        return value
    }

    companion object {
        val fromAttribute = IntrinsicAttributes.values().associateBy { it.relatedAttribute }
    }


}