package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.converters

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

enum class BasicArtifactConverters : ArtifactConverterInterface {
    ENTITIES_POSITION {
        override val customData: Int
            get() = 6000

        override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
            input.locations = input.entities.map { it.location }
        }
    },
    ENTITY_DIRECTION {
        override val customData: Int
            get() = 6004

        override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
            input.vectors = input.entities.map { it.location.direction }
        }
    },
    POSITION_BELOW {
        override val customData: Int
            get() = 6005

        override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
            input.locations = input.locations.map { it.subtract(Vector(0, 1, 0)) }
        }
    },
    POSITION_ABOVE {
        override val customData: Int
            get() = 6006

        override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
            input.locations = input.locations.map { it.add(Vector(0, 1, 0)) }
        }
    },
    POSITION_IN_FRONT {
        override val customData: Int
            get() = 6007

        override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
            input.locations =
                input.locations.zip(input.vectors).map { it.first.add(it.second.multiply(Vector(1, 0, 1)).normalize()) }
        }
    },
    NONE {
        override val customData: Int
            get() = 5999
    };

    companion object {

        private val map = BasicArtifactConverters.values().associateBy(BasicArtifactConverters::customData)
        fun getConverter(itemStack: ItemStack): BasicArtifactConverters? {
            return if (itemStack.itemMeta != null && itemStack.itemMeta.hasCustomModelData()) {
                map[itemStack.itemMeta.customModelData]
            } else {
                null
            }
        }
    }

    abstract val customData: Int

    override fun convertInput(itemStack: ItemStack, input: ArtifactInput) {
        // Default : do nothing
    }
}