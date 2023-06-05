package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import org.bukkit.Location
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
            input.entities = (0 until inputRune.amount).map { caster.getSelf() }
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
            input.entities = (0 until inputRune.amount).map { entitySight }
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
            input.locations = (0 until inputRune.amount).map { locationSight }
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
            input.locations = (0 until inputRune.amount).map { locationSight }
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

            input.locations = (0 until inputRune.amount).map { forwardEyeLocation }
            input.vectors = (0 until inputRune.amount).map { caster.getLocation().direction }
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
            input.vectors = (0 until inputRune.amount).map { caster.getLocation().direction }
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
    ENTITIES_DIRECTION {
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
    POSITIONS_BELOW {
        override val customData: Int
            get() = 6005

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations = input.locations.map { it.subtract(Vector(0, inputRune.amount, 0)) }
        }
    },
    POSITIONS_ABOVE {
        override val customData: Int
            get() = 6006

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations = input.locations.map { it.add(Vector(0, inputRune.amount, 0)) }
        }
    },
    POSITIONS_IN_FRONT {
        override val customData: Int
            get() = 6007

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations =
                input.locations.zip(input.vectors)
                    .map { it.first.add(it.second.multiply(Vector(1, 0, 1)).normalize().multiply(inputRune.amount)) }
        }
    },
    POSITION_AROUND_FLAT {
        override val customData: Int
            get() = 6008

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.locations = input.locations.map { locationAroundFlat(it, inputRune.amount) }.flatten()
        }

        private fun locationAroundFlat(location: Location, radius: Int): List<Location> {
            return (-radius..radius).map { (-radius..radius).map { itt -> Pair(it, itt) } }.flatten()
                .map { location.clone().add(Vector(it.first, 0, it.second)) }
        }
    },
    ENTITIES_AROUND {
        override val customData: Int
            get() = 6008

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val a = inputRune.amount.toDouble()
            input.entities = input.entities.map { it.getNearbyEntities(a, a, a) }.flatten()
        }


    },
    DOWN_DIRECTION {
        override val customData: Int
            get() = 6009

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = (0 until inputRune.amount).map { Vector(0, -1, 0) }
        }
    },
    UP_DIRECTION {
        override val customData: Int
            get() = 6010

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = (0 until inputRune.amount).map { Vector(0, 1, 0) }
        }
    },
    INVERT_DIRECTION {
        override val customData: Int
            get() = 6011

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = input.vectors.map { it.multiply(-1) }
        }
    },
    MULTIPLY_DIRECTION {
        override val customData: Int
            get() = 6012

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = input.vectors.map { it.multiply(inputRune.amount) }
        }
    },
    DIVIDE_DIRECTION {
        override val customData: Int
            get() = 6013

        override fun enrichArtifactInput(
            inputRune: ItemStack,
            caster: Caster,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.vectors = input.vectors.map { it.multiply(1 / inputRune.amount) }
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