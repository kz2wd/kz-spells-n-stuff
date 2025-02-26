package com.cludivers.kz2wdprison.modules.shards

import com.cludivers.kz2wdprison.framework.utils.Utils.buildItemStack
import com.cludivers.kz2wdprison.modules.artifact.ArtifactItemsTextures
import com.cludivers.kz2wdprison.modules.artifact.runes.RunesBehaviors
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val texture = ArtifactItemsTextures.SHARDS
        override val itemStack: ItemStack = run { setItemStack("Shards") }
    },
    FIRE_SPARK {
        override val texture = ArtifactItemsTextures.FIRE_SPARK
        override val itemStack: ItemStack = run { setItemStack("Fire Shard") }
    },

    UP_RUNE {
        override val texture = ArtifactItemsTextures.UP_RUNE
        override val itemStack: ItemStack = run { setItemStack("Up Rune") }
    },
    DOWN_RUNE {
        override val texture = ArtifactItemsTextures.DOWN_RUNE
        override val itemStack: ItemStack = run { setItemStack("Down Rune") }
    },
    LEFT_RUNE {
        override val texture = ArtifactItemsTextures.LEFT_RUNE
        override val itemStack: ItemStack = run { setItemStack("Left Rune") }
    },
    RIGHT_RUNE {
        override val texture = ArtifactItemsTextures.RIGHT_RUNE
        override val itemStack: ItemStack = run { setItemStack("Right Rune") }
    },
    FRONT_RUNE {
        override val texture = ArtifactItemsTextures.FRONT_RUNE
        override val itemStack: ItemStack = run { setItemStack("Forward Rune") }
    },
    BACK_RUNE {
        override val texture = ArtifactItemsTextures.BACK_RUNE
        override val itemStack: ItemStack = run { setItemStack("Backward Rune") }
    },

    // Entities
    ENTITY_CASTER_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_CASTER
        override val texture = ArtifactItemsTextures.ENTITY_CASTER
        override val itemStack: ItemStack = run { setItemStack("Caster") }
    },
    ENTITY_ATTACKER_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_ATTACKER
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Attacker") }
    },
    ENTITY_ATTACKED_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITY_ATTACKED
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Attacked") }
    },
    ENTITY_IN_SIGHT_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_IN_SIGHT
        override val texture = ArtifactItemsTextures.ENTITY_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Entity In Sight") }
    },
    ENTITY_AROUND_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_AROUND
        override val texture = ArtifactItemsTextures.ENTITIES_AROUND
        override val itemStack: ItemStack = run { setItemStack("Entities Around") }
    },
    ENTITY_REMOVE_RUNE {
        override val runeBehavior = RunesBehaviors.REMOVE_ENTITY
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Entity Remove") }
    },
    ENTITY_COPY_RUNE {
        override val runeBehavior = RunesBehaviors.COPY_ENTITY
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Entity Copy") }
    },

    // Locations
    ENTITIES_LOCATION_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_LOCATION
        override val texture = ArtifactItemsTextures.ENTITIES_LOCATION
        override val itemStack: ItemStack = run { setItemStack("Entities' Locations") }
    },
    LOCATION_SIGHT_RUNE {
        override val texture = ArtifactItemsTextures.LOCATION_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Location Sighted") }
        override val runeBehavior = RunesBehaviors.LOCATION_SIGHT
    },
    EMPTY_LOCATION_SIGHT_RUNE {
        override val runeBehavior = RunesBehaviors.EMPTY_LOCATION_SIGHT
        override val texture = ArtifactItemsTextures.EMPTY_LOCATION_SIGHT
        override val itemStack: ItemStack = run { setItemStack("Empty Location Sighted") }
    },
    LOCATION_BELOW_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_BELOW
        override val texture = ArtifactItemsTextures.LOCATIONS_BELOW
        override val itemStack: ItemStack = run { setItemStack("Locations Below") }
    },
    LOCATION_ABOVE_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_ABOVE
        override val texture = ArtifactItemsTextures.LOCATIONS_ABOVE
        override val itemStack: ItemStack = run { setItemStack("Locations Above") }
    },
    LOCATION_IN_FRONT_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_IN_FRONT
        override val texture = ArtifactItemsTextures.LOCATIONS_IN_FRONT
        override val itemStack: ItemStack = run { setItemStack("Locations In Front") }
    },
    LOCATION_BEHIND_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATIONS_BEHIND
        override val texture = ArtifactItemsTextures.LOCATIONS_BEHIND
        override val itemStack: ItemStack = run { setItemStack("Locations Behind") }
    },
    LOCATION_AROUND_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATION_AROUND
        override val texture = ArtifactItemsTextures.LOCATION_AROUND
        override val itemStack: ItemStack = run { setItemStack("Locations Around") }
    },
    LOCATION_AROUND_FLAT_RUNE {
        override val runeBehavior = RunesBehaviors.LOCATION_AROUND_FLAT
        override val texture = ArtifactItemsTextures.LOCATION_AROUND_FLAT
        override val itemStack: ItemStack = run { setItemStack("Ground Locations Around") }
    },
    LOCATION_REMOVE_RUNE {
        override val runeBehavior = RunesBehaviors.REMOVE_LOCATION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Locations Remover") }
    },
    LOCATION_COPY_RUNE {
        override val runeBehavior = RunesBehaviors.COPY_LOCATION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Location Copy") }
    },

    // Directions
    CASTER_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.CASTER_DIRECTION
        override val texture = ArtifactItemsTextures.CASTER_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Caster's Direction") }
    },
    ENTITIES_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.ENTITIES_DIRECTION
        override val texture = ArtifactItemsTextures.ENTITIES_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Entities' Direction") }
    },
    DOWN_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.DOWN_DIRECTION
        override val texture = ArtifactItemsTextures.DOWN_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction Down") }
    },
    UP_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.UP_DIRECTION
        override val texture = ArtifactItemsTextures.UP_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Direction Up") }
    },
    INVERT_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.INVERT_DIRECTION
        override val texture = ArtifactItemsTextures.INVERT_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Inverse Directions") }
    },
    DOUBLE_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.DOUBLE_DIRECTION
        override val texture = ArtifactItemsTextures.DOUBLE_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Doubled Directions") }
    },
    HALF_DIRECTION_RUNE {
        override val runeBehavior = RunesBehaviors.HALF_DIRECTION
        override val texture = ArtifactItemsTextures.HALF_DIRECTION
        override val itemStack: ItemStack = run { setItemStack("Halved Directions") }
    },
    DIRECTION_REMOVE_RUNE {
        override val runeBehavior = RunesBehaviors.REMOVE_DIRECTION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Direction Remover") }
    },
    DIRECTION_COPY_RUNE {
        override val runeBehavior = RunesBehaviors.COPY_DIRECTION
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Direction Copy") }
    },

    // Projectile casting
    CASTER_PROJECTILE_RUNE {
        override val runeBehavior = RunesBehaviors.CASTER_PROJECTILE
        override val texture = ArtifactItemsTextures.CASTER_PROJECTILE
        override val itemStack: ItemStack = run { setItemStack("Caster's Projectile") }
    },
    PROJECTILE_CASTING_RUNE {
        override val runeBehavior = RunesBehaviors.PROJECTILE_CASTING
        override val texture = ArtifactItemsTextures.PROJECTILE_CASTING
        override val itemStack: ItemStack = run { setItemStack("Projectile Casting") }
    },

    LIGHTNING_SPARK {
        override val runeBehavior = RunesBehaviors.LIGHTNING_SPARK
        override val texture = ArtifactItemsTextures.LIGHTNING_SPARK
        override val itemStack: ItemStack = run { setItemStack("Lightning Shard") }
    },
    MOVE_RUNE {
        override val runeBehavior = RunesBehaviors.MOVE_RUNE
        override val texture = ArtifactItemsTextures.MOVE_RUNE
        override val itemStack: ItemStack = run { setItemStack("Move Rune") }
    },
    TELEPORT_RUNE {
        override val runeBehavior = RunesBehaviors.TELEPORT_RUNE
        override val texture = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack = run { setItemStack("Teleportation Rune") }
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