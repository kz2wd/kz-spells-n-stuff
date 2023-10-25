package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactItemsTextures
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.math.min

enum class EnrichingArtifactRunes : ArtifactRuneInterface {
    //<editor-fold desc="ENTITY_CASTER" defaultstate="collapsed">
    ENTITY_CASTER {
        override val texture
            get() = ArtifactItemsTextures.ENTITY_CASTER

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Lanceur"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities = (0 until inputRune.amount).map { artifactActivator.getSelf() }
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_IN_SIGHT" defaultstate="collapsed">
    ENTITIES_IN_SIGHT {

        override val requirement = RuneRequirements.ENTITY
        override val texture
            get() = ArtifactItemsTextures.ENTITY_SIGHT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Entités à vue"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities =
                input.entities.filterIsInstance<LivingEntity>().mapNotNull { it.getTargetEntity(inputRune.amount) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATION_SIGHT" defaultstate="collapsed">
    LOCATION_SIGHT {

        override val requirement: RuneRequirements = RuneRequirements.ENTITY

        override val texture
            get() = ArtifactItemsTextures.LOCATION_SIGHT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Position à vue"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.entities.filterIsInstance<LivingEntity>()
                .mapNotNull { it.getTargetBlockExact(inputRune.amount)?.location }
        }
    },

    //</editor-fold>
    //<editor-fold desc="EMPTY_LOCATION_SIGHT" defaultstate="collapsed">
    EMPTY_LOCATION_SIGHT {

        override val requirement: RuneRequirements = RuneRequirements.ENTITY
        override val texture
            get() = ArtifactItemsTextures.EMPTY_LOCATION_SIGHT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Vide à vue"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.entities.filterIsInstance<LivingEntity>()
                .mapNotNull { it.getLastTwoTargetBlocks(null, inputRune.amount)[0]?.location }
        }
    },

    //</editor-fold>
    //<editor-fold desc="PROJECTILE_CASTING" defaultstate="collapsed">
    PROJECTILE_CASTING {
        override val texture
            get() = ArtifactItemsTextures.PROJECTILE_CASTING

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Entité tire"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            if (input.entities.isEmpty()) {
                input.entities = listOf(artifactActivator.getSelf())
            }
            input.locations = input.entities.map { it.location }
            input.directions = input.entities.map { it.location.direction }
        }

    },

    //</editor-fold>
    //<editor-fold desc="CASTER_PROJECTILE" defaultstate="collapsed">
    CASTER_PROJECTILE {
        override val texture
            get() = ArtifactItemsTextures.CASTER_PROJECTILE

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("lanceur tire"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            var forwardEyeLocation = (artifactActivator.getSelf() as Player).eyeLocation
            forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)
            input.locations = (0 until inputRune.amount).map { forwardEyeLocation }
            input.directions = (0 until inputRune.amount).map { artifactActivator.getLocation().direction }
        }
    },

    //</editor-fold>
    //<editor-fold desc="CASTER_DIRECTION" defaultstate="collapsed">
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

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = (0 until inputRune.amount).map { artifactActivator.getLocation().direction }
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_POSITION">
    ENTITIES_POSITION {

        override val requirement = RuneRequirements.ENTITY
        override val texture
            get() = ArtifactItemsTextures.ENTITIES_POSITION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions des entités"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.entities.map { it.location }
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_DIRECTION" defaultstate="collapsed">
    ENTITIES_DIRECTION {
        override val requirement = RuneRequirements.ENTITY
        override val texture
            get() = ArtifactItemsTextures.ENTITIES_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Directions des entités"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = input.entities.map { it.location.direction }
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_BELOW" defaultstate="collapsed">
    LOCATIONS_BELOW {
        override val requirement = RuneRequirements.LOCATION
        override val texture
            get() = ArtifactItemsTextures.LOCATIONS_BELOW

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions en-bas"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.locations.map { it.subtract(Vector(0, inputRune.amount, 0)) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_ABOVE" defaultstate="collapsed">
    LOCATIONS_ABOVE {
        override val requirement = RuneRequirements.LOCATION
        override val texture
            get() = ArtifactItemsTextures.LOCATIONS_ABOVE

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions en-haut"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations = input.locations.map { it.add(Vector(0, inputRune.amount, 0)) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_IN_FRONT" defaultstate="collapsed">
    LOCATIONS_IN_FRONT {
        override val requirement = RuneRequirements.LOCATION_DIRECTION
        override val texture
            get() = ArtifactItemsTextures.LOCATIONS_IN_FRONT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions devant"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations =
                input.locations.zip(input.directions)
                    .map { it.first.add(it.second.multiply(Vector(1, 0, 1)).normalize().multiply(inputRune.amount)) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATI0NS_BEHIND" defaultstate="collapsed">
    LOCATIONS_BEHIND {
        override val requirement = RuneRequirements.LOCATION_DIRECTION
        override val texture
            get() = ArtifactItemsTextures.LOCATIONS_BEHIND

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions devant"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations =
                input.locations.zip(input.directions)
                    .map {
                        it.first.subtract(
                            it.second.multiply(Vector(1, 0, 1)).normalize().multiply(inputRune.amount)
                        )
                    }
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATION_AROUND" defaultstate="collapsed">
    LOCATION_AROUND {
        override val requirement = RuneRequirements.LOCATION
        override val texture
            get() = ArtifactItemsTextures.LOCATION_AROUND

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions autour"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations =
                locationAround(input.locations.random(), min(inputRune.amount, 3)) // Hard cap to prevent lag
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATION_AROUND_FLAT" defaultstate="collapsed">
    LOCATION_AROUND_FLAT {
        override val requirement = RuneRequirements.LOCATION
        override val texture
            get() = ArtifactItemsTextures.LOCATION_AROUND_FLAT

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Positions autour plates"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.locations =
                locationAroundFlat(input.locations.random(), min(inputRune.amount, 10)) // Hard cap to prevent lag
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_AROUND", defaultstate="collapsed">
    ENTITIES_AROUND {
        override val requirement = RuneRequirements.ENTITY
        override val texture
            get() = ArtifactItemsTextures.ENTITIES_AROUND

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(
                Component.text("Entités aux alentours des entités"),
                Material.IRON_NUGGET,
                texture.customData
            )
        }

        override fun artifactActivationWithRequirements(
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

    //</editor-fold>
    //<editor-fold desc="DOWN_DIRECTION" defaultstate="collapsed">
    DOWN_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.DOWN_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Bas"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = (0 until inputRune.amount).map { Vector(0, -1, 0) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="UP_DIRECTION" defaultstate="collapsed">
    UP_DIRECTION {
        override val texture
            get() = ArtifactItemsTextures.UP_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Haut"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = (0 until inputRune.amount).map { Vector(0, 1, 0) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="INVERT_DIRECTION" defaultstate="collapsed">
    INVERT_DIRECTION {
        override val requirement = RuneRequirements.DIRECTION
        override val texture
            get() = ArtifactItemsTextures.INVERT_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Invertion des directions"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = input.directions.map { it.multiply(-1) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="MULTIPLY_DIRECTION" defaultstate="collapsed">
    MULTIPLY_DIRECTION {
        override val requirement = RuneRequirements.DIRECTION
        override val texture
            get() = ArtifactItemsTextures.MULTIPLY_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(
                Component.text("Multiplication des directions"),
                Material.IRON_NUGGET,
                texture.customData
            )
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = input.directions.map { it.multiply(inputRune.amount) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="DIVIDE_DIRECTION" defaultstate="collapsed">
    DIVIDE_DIRECTION {
        override val requirement = RuneRequirements.DIRECTION
        override val texture
            get() = ArtifactItemsTextures.DIVIDE_DIRECTION

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Division des directions"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.directions = input.directions.map { it.multiply(1f / (inputRune.amount + 1)) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="NONE" defaultstate="collapsed">
    NONE {
        override val texture
            get() = ArtifactItemsTextures.NONE

        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Vide"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            return
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITY_ATTACKER" defaultstate="collapsed">
    ATTACKER {
        override val texture: ArtifactItemsTextures
            get() = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Attaquant"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities = (0 until inputRune.amount).mapNotNull { artifactActivator.getAttacker() }
        }

    },

    //</editor-fold>
    //<editor-fold desc="ENTITY_ATTACKED" defaultstate="collapsed">
    ATTACKED {
        override val texture: ArtifactItemsTextures
            get() = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run {
            Utils.buildItemStack(Component.text("Attaqué"), Material.IRON_NUGGET, texture.customData)
        }

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?
        ) {
            input.entities = (0 until inputRune.amount).mapNotNull { artifactActivator.getAttacked() }
        }
    },
    //</editor-fold>
    ;

    override fun processArtifactActivation(
        inputRune: ItemStack,
        artifactActivator: ArtifactActivator,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>,
        player: Player?
    ) {
        requirement.ensureRequirement(input, artifactActivator)
        artifactActivationWithRequirements(inputRune, artifactActivator, input, inputsTrace, player)
    }

    protected abstract fun artifactActivationWithRequirements(
        inputRune: ItemStack,
        artifactActivator: ArtifactActivator,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>,
        player: Player?
    )

    protected open val requirement: RuneRequirements = RuneRequirements.NONE
    abstract val texture: ArtifactItemsTextures
    abstract val itemStack: ItemStack


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

        internal fun locationAroundFlat(location: Location, radius: Int): List<Location> {
            return (-radius..radius).map { (-radius..radius).map { itt -> Pair(it, itt) } }.flatten()
                .map { location.clone().add(Vector(it.first, 0, it.second)) }
        }


        internal fun locationAround(location: Location, radius: Int): List<Location> {
            return (-radius..radius).map { locationAroundFlat(location.clone().add(Vector(0, it, 0)), radius) }
                .flatten()
        }
    }



}