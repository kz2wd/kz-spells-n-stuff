package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

enum class BasicInputRunes : ArtifactInputInterface {
    ENTITY_CASTER {
        override val customData: Int
            get() = 5000

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.entities = listOf(caster.getSelf())
        }
    },
    ENTITY_SIGHT {
        override val customData: Int
            get() = 5001

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val entitySight = caster.getSightEntity(caster.maxSightDistance()) ?: return
            input.entities = listOf(entitySight)
        }
    },
    LOCATION_SIGHT {
        override val customData: Int
            get() = 5003

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val locationSight =
                caster.getSightBlock(caster.maxSightDistance())?.location ?: return
            input.locations = listOf(locationSight)
        }
    },
    EMPTY_LOCATION_SIGHT {
        override val customData: Int
            get() = 5004

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val locationSight =
                caster.getSightAirBlock(caster.maxSightDistance())?.location ?: return
            input.locations = listOf(locationSight)
        }
    },
    PROJECTILE_CASTING {
        override val customData: Int
            get() = 5005

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            var forwardEyeLocation = (caster.getSelf() as Player).eyeLocation
            forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)

            input.locations = listOf(forwardEyeLocation)
            input.vectors = listOf(caster.getLocation().direction)
        }

    },
    CASTER_DIRECTION {
        override val customData: Int
            get() = 5006

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = listOf(caster.getLocation().direction)
        }
    },
    ENTITIES_POSITION {
        override val customData: Int
            get() = 6000

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations = input.entities.map { it.location }
        }
    },
    ENTITY_DIRECTION {
        override val customData: Int
            get() = 6004

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = input.entities.map { it.location.direction }
        }
    },
    POSITION_BELOW {
        override val customData: Int
            get() = 6005

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations = input.locations.map { it.subtract(Vector(0, 1, 0)) }
        }
    },
    POSITION_ABOVE {
        override val customData: Int
            get() = 6006

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations = input.locations.map { it.add(Vector(0, 1, 0)) }
        }
    },
    POSITION_IN_FRONT {
        override val customData: Int
            get() = 6007

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations =
                input.locations.zip(input.vectors).map { it.first.add(it.second.multiply(Vector(1, 0, 1)).normalize()) }
        }
    },
    NONE {
        override val customData: Int
            get() = 4999

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            return
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