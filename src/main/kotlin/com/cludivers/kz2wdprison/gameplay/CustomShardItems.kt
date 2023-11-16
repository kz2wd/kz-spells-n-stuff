package com.cludivers.kz2wdprison.gameplay

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactItemsTextures
import com.cludivers.kz2wdprison.gameplay.artifact.runes.RunesBehaviors
import com.cludivers.kz2wdprison.gameplay.utils.Utils.buildItemStack
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val texture = ArtifactItemsTextures.SHARDS
        override val itemStack: ItemStack = run { setItemStack("Fragments") }
    },
    FIRE_SPARK {
        override val texture = ArtifactItemsTextures.FIRE_SPARK
        override val itemStack: ItemStack = run { setItemStack("Fragment de Feu") }
    },
    LIGHTNING_SPARK {
        override val runeBehavior = RunesBehaviors.LIGHTNING_SPARK
        override val texture = ArtifactItemsTextures.LIGHTNING_SPARK
        override val itemStack: ItemStack = run { setItemStack("Fragment d'Eclair") }
    },
    UP_RUNE {
        override val texture = ArtifactItemsTextures.UP_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Haut") }
    },
    DOWN_RUNE {
        override val texture = ArtifactItemsTextures.DOWN_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Bas") }
    },
    LEFT_RUNE {
        override val texture = ArtifactItemsTextures.LEFT_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Gauche") }
    },
    RIGHT_RUNE {
        override val texture = ArtifactItemsTextures.RIGHT_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Droite") }
    },
    FRONT_RUNE {
        override val texture = ArtifactItemsTextures.FRONT_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Avant") }
    },
    BACK_RUNE {
        override val texture = ArtifactItemsTextures.BACK_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Arrière") }
    },
    MOVE_RUNE {
        override val runeBehavior = RunesBehaviors.MOVE_RUNE
        override val texture = ArtifactItemsTextures.MOVE_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Mouvement") }
    },
    ENTITY_CASTER_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_CASTER
        override val texture = ArtifactItemsTextures.ENTITY_CASTER
        override val itemStack: ItemStack = run { setItemStack("Catalyseur") }
    },
    ENTITIES_IN_SIGHT_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_IN_SIGHT
        override val texture = ArtifactItemsTextures.ENTITY_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Entité à vue") }
    },
    LOCATION_SIGHT_RUNE {
        override val texture = ArtifactItemsTextures.LOCATION_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Position à vue") }
        override val runeBehavior = RunesBehaviors.LOCATION_SIGHT
    },
    EMPTY_LOCATION_SIGHT_RUNE {
        override val runeBehavior = RunesBehaviors.EMPTY_LOCATION_SIGHT
        override val texture = ArtifactItemsTextures.EMPTY_LOCATION_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Position Libre à vue") }
    },
    PROJECTILE_CASTING_RUNE {
        override val runeBehavior = RunesBehaviors.PROJECTILE_CASTING
        override val texture = ArtifactItemsTextures.PROJECTILE_CASTING
        override val itemStack: ItemStack = run { setItemStack("Lancement de Projectile") }
    },
    CASTER_PROJECTILE_RUNE {
        override val runeBehavior = RunesBehaviors.CASTER_PROJECTILE
        override val texture = ArtifactItemsTextures.CASTER_PROJECTILE
        override val itemStack: ItemStack = run { setItemStack("Projectile à partir du Catalyseur") }
    },
    CASTER_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.CASTER_DIRECTION
        override val texture = ArtifactItemsTextures.CASTER_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction du Catalyseur") }
    },
    ENTITIES_POSITION_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_POSITION
        override val texture = ArtifactItemsTextures.ENTITIES_POSITION
        override val itemStack: ItemStack = run { setItemStack("Position des Entités") }
    },
    ENTITIES_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_DIRECTION
        override val texture = ArtifactItemsTextures.ENTITIES_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction des Entités") }
    },
    LOCATIONS_BELOW_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_BELOW
        override val texture = ArtifactItemsTextures.LOCATIONS_BELOW
        override val itemStack: ItemStack = run { setItemStack("Positions en-dessous") }
    },
    LOCATIONS_ABOVE_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_ABOVE
        override val texture = ArtifactItemsTextures.LOCATIONS_ABOVE
        override val itemStack: ItemStack = run { setItemStack("Positions au-dessus") }
    },
    LOCATIONS_IN_FRONT_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_IN_FRONT
        override val texture = ArtifactItemsTextures.LOCATIONS_IN_FRONT
        override val itemStack: ItemStack = run { setItemStack("Positions devant") }
    },
    LOCATIONS_BEHIND_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_BEHIND
        override val texture = ArtifactItemsTextures.LOCATIONS_BEHIND
        override val itemStack: ItemStack = run { setItemStack("Positions derrière") }
    },
    LOCATION_AROUND_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATION_AROUND
        override val texture = ArtifactItemsTextures.LOCATION_AROUND
        override val itemStack: ItemStack = run { setItemStack("Positions Autour") }
    },
    LOCATION_AROUND_FLAT_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATION_AROUND_FLAT
        override val texture = ArtifactItemsTextures.LOCATION_AROUND_FLAT
        override val itemStack: ItemStack = run { setItemStack("Positions au Sol Autour") }
    },
    ENTITIES_AROUND_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_AROUND
        override val texture = ArtifactItemsTextures.ENTITIES_AROUND
        override val itemStack: ItemStack = run { setItemStack("Entités Autour") }
    },
    DOWN_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.DOWN_DIRECTION
        override val texture = ArtifactItemsTextures.DOWN_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction Bas") }
    },
    UP_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.UP_DIRECTION
        override val texture = ArtifactItemsTextures.UP_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction Haut") }
    },
    INVERT_DIRECTIONS_RUNE {
        override val runeBehavior = RunesBehaviors.INVERT_DIRECTION
        override val texture = ArtifactItemsTextures.INVERT_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Inversion des Directions") }
    },
    DOUBLE_DIRECTIONS_RUNE {
        override val runeBehavior = RunesBehaviors.DOUBLE_DIRECTION
        override val texture = ArtifactItemsTextures.MULTIPLY_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Multiplication des Directions") }
    },
    HALF_DIRECTIONS_RUNE {
        override val runeBehavior = RunesBehaviors.HALF_DIRECTION
        override val texture = ArtifactItemsTextures.HALF_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Division des Directions") }
    },
    ENTITY_ATTACKER_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_ATTACKER
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Attaquant") }
    },
    ENTITY_ATTACKED_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_ATTACKED
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Attaqué") }
    },
    ;

    open val runeBehavior: RunesBehaviors = RunesBehaviors.NONE

    companion object {
        private val allCustomItems: MutableMap<ItemStack, CustomShardItems> =
            CustomShardItems.values().associateBy(CustomShardItems::itemStack).toMutableMap()

        fun getCustomItemStack(itemStack: ItemStack): CustomShardItems? {
            return allCustomItems[itemStack.asOne()]
        }
    }

    internal fun setItemStack(name: String, material: Material = Material.IRON_NUGGET): ItemStack {
        return buildItemStack(Component.text(name), material, texture.customData)
    }

    protected abstract val texture: ArtifactItemsTextures
    abstract val itemStack: ItemStack
}