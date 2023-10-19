package com.cludivers.kz2wdprison.gameplay

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactItemsTextures
import com.cludivers.kz2wdprison.gameplay.utils.Utils.buildItemStack
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val texture = ArtifactItemsTextures.SHARDS
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Shards"), Material.IRON_NUGGET, texture.customData)
        }
    },
    FIRE_SPARK {
        override val texture = ArtifactItemsTextures.FIRE_SPARK
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Fire Spark"), Material.IRON_NUGGET, texture.customData)

        }
    },
    LIGHTNING_SPARK {
        override val texture = ArtifactItemsTextures.LIGHTNING_SPARK
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Lighting Spark"), Material.IRON_NUGGET, texture.customData)
        }
    },
    UP_RUNE {
        override val texture = ArtifactItemsTextures.UP_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Up Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    DOWN_RUNE {
        override val texture = ArtifactItemsTextures.DOWN_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Down Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    LEFT_RUNE {
        override val texture = ArtifactItemsTextures.LEFT_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Left Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    RIGHT_RUNE {
        override val texture = ArtifactItemsTextures.RIGHT_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Right Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    FRONT_RUNE {
        override val texture = ArtifactItemsTextures.FRONT_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Front Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    BACK_RUNE {
        override val texture = ArtifactItemsTextures.BACK_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Back Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    MOVE_RUNE {
        override val texture = ArtifactItemsTextures.MOVE_RUNE
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Move Rune"), Material.IRON_NUGGET, texture.customData)
        }
    },
    COMPLEX_INPUT_RUNE {
        override val texture: ArtifactItemsTextures
            get() = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack
            get() = buildItemStack(Component.text("Unknown rune"), Material.IRON_NUGGET, texture.customData)
    },
    COMPLEX_EFFECT_RUNE {
        override val texture: ArtifactItemsTextures
            get() = ArtifactItemsTextures.NONE
        override val itemStack: ItemStack
            get() = buildItemStack(Component.text("Unknown rune"), Material.IRON_NUGGET, texture.customData)
    }
    ;

    companion object {
        private val allCustomItems: MutableMap<ItemStack, CustomShardItems> =
            CustomShardItems.values().associateBy(CustomShardItems::itemStack).toMutableMap()

        fun getCustomItemStack(itemStack: ItemStack): CustomShardItems? {
            return allCustomItems[itemStack]
        }
    }

    protected abstract val texture: ArtifactItemsTextures
    abstract val itemStack: ItemStack
}