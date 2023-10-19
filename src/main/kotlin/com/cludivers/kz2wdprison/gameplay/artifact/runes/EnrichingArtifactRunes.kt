package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactItemsTextures
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

enum class EnrichingArtifactRunes : ArtifactRuneInterface {
    ENTITY_CASTER {
        override val texture
            get() = ArtifactItemsTextures.ENTITY_CASTER

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Lanceur"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities = (0 until inputRune.amount).map { artifactActivator.getSelf() }
        }
    },
    ENTITY_SIGHT {
        override val texture
            get() = ArtifactItemsTextures.ENTITY_SIGHT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Entité à vue"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            val entitySight = artifactActivator.getSightEntity(artifactActivator.maxSightDistance()) ?: return
            input.entities = (0 until inputRune.amount).map { entitySight }
        }
    },
    LOCATION_SIGHT {
        override val texture
            get() = ArtifactItemsTextures.LOCATION_SIGHT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Position à vue"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            val locationSight =
                artifactActivator.getSightBlock(artifactActivator.maxSightDistance())?.location ?: return
            input.locations = (0 until inputRune.amount).map { locationSight }
        }
    },
    EMPTY_LOCATION_SIGHT {
        override val texture
            get() = ArtifactItemsTextures.EMPTY_LOCATION_SIGHT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Vide à vue"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            val locationSight =
                artifactActivator.getSightAirBlock(artifactActivator.maxSightDistance())?.location ?: return
            input.locations = (0 until inputRune.amount).map { locationSight }
        }
    },
    PROJECTILE_CASTING {
        override val texture
            get() = ArtifactItemsTextures.PROJECTILE_CASTING

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Lancement de projectile"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            var forwardEyeLocation = (artifactActivator.getSelf() as Player).eyeLocation
            forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)

            input.locations = (0 until inputRune.amount).map { forwardEyeLocation }
            input.vectors = (0 until inputRune.amount).map { artifactActivator.getLocation().direction }
        }

    },
    CASTER_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.CASTER_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(
                Component.text("Direction du lanceur"),
                Material.IRON_NUGGET,
                texture.customData
            )
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = (0 until inputRune.amount).map { artifactActivator.getLocation().direction }
        }
    },
    ENTITIES_POSITION {
        override val texture
            get() = ArtifactItemsTextures.ENTITIES_POSITION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions des entités"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.entities.map { it.location }
        }
    },
    ENTITIES_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.ENTITIES_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Directions des entités"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = input.entities.map { it.location.direction }
        }
    },
    POSITIONS_BELOW {
        override val texture
            get() = ArtifactItemsTextures.POSITIONS_BELOW

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions en-bas"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.locations.map { it.subtract(Vector(0, inputRune.amount, 0)) }
        }
    },
    POSITIONS_ABOVE {
        override val texture
            get() = ArtifactItemsTextures.POSITIONS_ABOVE

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions en-haut"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.locations.map { it.add(Vector(0, inputRune.amount, 0)) }
        }
    },
    POSITIONS_IN_FRONT {
        override val texture
            get() = ArtifactItemsTextures.POSITIONS_IN_FRONT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions devant"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations =
                input.locations.zip(input.vectors)
                    .map { it.first.add(it.second.multiply(Vector(1, 0, 1)).normalize().multiply(inputRune.amount)) }
        }
    },
    POSITION_AROUND_FLAT {
        override val texture
            get() = ArtifactItemsTextures.POSITION_AROUND_FLAT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions autour plates"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = locationAroundFlat(input.locations.random(), inputRune.amount)
        }

        private fun locationAroundFlat(location: Location, radius: Int): List<Location> {
            return (-radius..radius).map { (-radius..radius).map { itt -> Pair(it, itt) } }.flatten()
                .map { location.clone().add(Vector(it.first, 0, it.second)) }
        }
    },
    ENTITIES_AROUND {
        override val texture
            get() = ArtifactItemsTextures.ENTITIES_AROUND

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(
                Component.text("Entités aux alentours des entités"),
                Material.IRON_NUGGET,
                texture.customData
            )
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            val a = inputRune.amount.toDouble()
            input.entities = input.entities.map { it.getNearbyEntities(a, a, a) }.flatten()
        }


    },
    DOWN_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.DOWN_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Bas"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = (0 until inputRune.amount).map { Vector(0, -1, 0) }
        }
    },
    UP_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.UP_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Haut"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = (0 until inputRune.amount).map { Vector(0, 1, 0) }
        }
    },
    INVERT_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.INVERT_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Invertion des directions"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = input.vectors.map { it.multiply(-1) }
        }
    },
    MULTIPLY_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.MULTIPLY_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(
                Component.text("Multiplication des directions"),
                Material.IRON_NUGGET,
                texture.customData
            )
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = input.vectors.map { it.multiply(inputRune.amount) }
        }
    },
    DIVIDE_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.DIVIDE_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Division des directions"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.vectors = input.vectors.map { it.multiply(1f / (inputRune.amount + 1)) }
        }
    },
    NONE {
        override val texture
            get() = ArtifactItemsTextures.NONE

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Vide"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            return
        }
    },
    ATTACKER {
        override val texture: ArtifactItemsTextures
            get() = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Attaquant"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities = (0 until inputRune.amount).mapNotNull { artifactActivator.getAttacker() }
        }

    },
    ATTACKED {
        override val texture: ArtifactItemsTextures
            get() = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Attaqué"), Material.IRON_NUGGET, texture.customData)
        }

        override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities = (0 until inputRune.amount).mapNotNull { artifactActivator.getAttacked() }
        }
    },
    ;

    companion object {
        private val map = EnrichingArtifactRunes.values().associateBy { it.itemStack }
        internal fun getArtifactRune(itemStack: ItemStack?): EnrichingArtifactRunes? {
            if (itemStack == null) {
                return null
            }
            return if (itemStack.itemMeta != null && itemStack.itemMeta.hasCustomModelData()) {
                map[itemStack.asOne()]
            } else {
                null
            }
        }
    }

    abstract val texture: ArtifactItemsTextures
    abstract val itemStack: ItemStack

}