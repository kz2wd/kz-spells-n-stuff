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

    // Entities
    ENTITY_CASTER_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_CASTER
        override val texture = ArtifactItemsTextures.ENTITY_CASTER
        override val itemStack: ItemStack = run { setItemStack("Catalyseur") }
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
    ENTITY_IN_SIGHT_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_IN_SIGHT
        override val texture = ArtifactItemsTextures.ENTITY_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Entité à vue") }
    },
    ENTITY_AROUND_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_AROUND
        override val texture = ArtifactItemsTextures.ENTITIES_AROUND
        override val itemStack: ItemStack = run { setItemStack("Entités Autour") }
    },
    ENTITY_REMOVE_RUNE {
        override val runeBehavior = RunesBehaviors.REMOVE_ENTITY
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Suppression d'entité") }
    },
    ENTITY_COPY_RUNE {
        override val runeBehavior = RunesBehaviors.COPY_ENTITY
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Copie d'entité") }
    },

    // Locations
    ENTITIES_LOCATION_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_LOCATION
        override val texture = ArtifactItemsTextures.ENTITIES_LOCATION
        override val itemStack: ItemStack = run { setItemStack("Position des Entités") }
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
    LOCATION_BELOW_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_BELOW
        override val texture = ArtifactItemsTextures.LOCATIONS_BELOW
        override val itemStack: ItemStack = run { setItemStack("Positions en-dessous") }
    },
    LOCATION_ABOVE_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_ABOVE
        override val texture = ArtifactItemsTextures.LOCATIONS_ABOVE
        override val itemStack: ItemStack = run { setItemStack("Positions au-dessus") }
    },
    LOCATION_IN_FRONT_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_IN_FRONT
        override val texture = ArtifactItemsTextures.LOCATIONS_IN_FRONT
        override val itemStack: ItemStack = run { setItemStack("Positions devant") }
    },
    LOCATION_BEHIND_RUNE {
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
    LOCATION_REMOVE_RUNE {
        override val runeBehavior = RunesBehaviors.REMOVE_LOCATION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Suppression de position") }
    },
    LOCATION_COPY_RUNE {
        override val runeBehavior = RunesBehaviors.COPY_LOCATION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Copie de position") }
    },

    // Directions
    CASTER_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.CASTER_DIRECTION
        override val texture = ArtifactItemsTextures.CASTER_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction du Catalyseur") }
    },
    ENTITIES_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_DIRECTION
        override val texture = ArtifactItemsTextures.ENTITIES_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction des Entités") }
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
    INVERT_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.INVERT_DIRECTION
        override val texture = ArtifactItemsTextures.INVERT_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Inversion des Directions") }
    },
    DOUBLE_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.DOUBLE_DIRECTION
        override val texture = ArtifactItemsTextures.DOUBLE_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Doublement des Directions") }
    },
    HALF_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.HALF_DIRECTION
        override val texture = ArtifactItemsTextures.HALF_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Division des Directions") }
    },
    DIRECTION_REMOVE_RUNE {
        override val runeBehavior = RunesBehaviors.REMOVE_DIRECTION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Suppression de direction") }
    },
    DIRECTION_COPY_RUNE {
        override val runeBehavior = RunesBehaviors.COPY_DIRECTION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Copie de direction") }
    },

    // Projectile casting
    CASTER_PROJECTILE_RUNE {
        override val runeBehavior = RunesBehaviors.CASTER_PROJECTILE
        override val texture = ArtifactItemsTextures.CASTER_PROJECTILE
        override val itemStack: ItemStack = run { setItemStack("Projectile à partir du Catalyseur") }
    },
    PROJECTILE_CASTING_RUNE {
        override val runeBehavior = RunesBehaviors.PROJECTILE_CASTING
        override val texture = ArtifactItemsTextures.PROJECTILE_CASTING
        override val itemStack: ItemStack = run { setItemStack("Lancement de Projectile") }
    },

    LIGHTNING_SPARK {
        override val runeBehavior = RunesBehaviors.LIGHTNING_SPARK
        override val texture = ArtifactItemsTextures.LIGHTNING_SPARK
        override val itemStack: ItemStack = run { setItemStack("Fragment d'Eclair") }
    },
    MOVE_RUNE {
        override val runeBehavior = RunesBehaviors.MOVE_RUNE
        override val texture = ArtifactItemsTextures.MOVE_RUNE
        override val itemStack: ItemStack = run { setItemStack("Rune Mouvement") }
    },
    TELEPORT_RUNE {
        override val runeBehavior = RunesBehaviors.TELEPORT_RUNE
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Rune Téleportation") }
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