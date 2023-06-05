package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class BasicInputRunes : ArtifactInputInterface {
    ENTITY_CASTER {
        override val customData: Int
            get() = 5000

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            return ArtifactInput(inFlow, entities = listOf(caster.getSelf()))
        }
    },
    ENTITY_SIGHT {
        override val customData: Int
            get() = 5001

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            val entitySight = caster.getSightEntity(caster.maxSightDistance()) ?: return ArtifactInput(inFlow)
            return ArtifactInput(inFlow, entities = listOf(entitySight))
        }
    },
    LOCATION_SIGHT {
        override val customData: Int
            get() = 5003

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            val locationSight =
                caster.getSightBlock(caster.maxSightDistance())?.location ?: return ArtifactInput(inFlow)
            return ArtifactInput(inFlow, listOf(locationSight))
        }
    },
    EMPTY_LOCATION_SIGHT {
        override val customData: Int
            get() = 5004

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            val locationSight =
                caster.getSightAirBlock(caster.maxSightDistance())?.location ?: return ArtifactInput(inFlow)
            return ArtifactInput(inFlow, listOf(locationSight))
        }
    },
    PROJECTILE_CASTING {
        override val customData: Int
            get() = 5005

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            var forwardEyeLocation = (caster.getSelf() as Player).eyeLocation
            forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)

            return ArtifactInput(inFlow, listOf(forwardEyeLocation), vectors = listOf(caster.getLocation().direction))
        }

    },
    CASTER_DIRECTION {
        override val customData: Int
            get() = 5006

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            return ArtifactInput(inFlow, vectors = listOf(caster.getLocation().direction))
        }
    },
    NONE {
        override val customData: Int
            get() = 4999

        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            return ArtifactInput(inFlow)
        }
    };

    companion object {
        private val map = BasicInputRunes.values().associateBy(BasicInputRunes::customData)
        fun getInputRune(itemStack: ItemStack?): BasicInputRunes? {
            if (itemStack == null) {
                return null
            }
            return if (itemStack.itemMeta != null && itemStack.itemMeta.hasCustomModelData()) {
                map[itemStack.itemMeta.customModelData]
            } else {
                null
            }
        }
    }
    abstract val customData: Int

}