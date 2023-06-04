package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.utils.Utils.buildItemStack
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val flag: Int = 10000
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Shards"), Material.IRON_NUGGET, flag)
        }
    },
    FIRE_SPARK {
        override val flag: Int = 10001
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Fire Spark"), Material.IRON_NUGGET, flag)

        }
    },
    LIGHTNING_SPARK {
        override val flag: Int = 10002
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Lighting Spark"), Material.IRON_NUGGET, flag)
        }
    },
    UP_RUNE {
        override val flag: Int = 10003
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Up Rune"), Material.IRON_NUGGET, flag)
        }
    },
    DOWN_RUNE {
        override val flag: Int = 10003
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Down Rune"), Material.IRON_NUGGET, flag)
        }
    },
    LEFT_RUNE {
        override val flag: Int = 10004
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Left Rune"), Material.IRON_NUGGET, flag)
        }
    },
    RIGHT_RUNE {
        override val flag: Int = 10004
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Right Rune"), Material.IRON_NUGGET, flag)
        }
    },
    FRONT_RUNE {
        override val flag: Int = 10005
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Front Rune"), Material.IRON_NUGGET, flag)
        }
    },
    BACK_RUNE {
        override val flag: Int = 10006
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Back Rune"), Material.IRON_NUGGET, flag)
        }
    },
    DASH_RUNE {
        override val flag: Int = 10007
        override val itemStack: ItemStack = run {
            buildItemStack(Component.text("Dash Rune"), Material.IRON_NUGGET, flag)
        }
    }

    ;

    companion object {
        private val map = CustomShardItems.values().associateBy(CustomShardItems::itemStack)
        fun getCustomItemStack(itemStack: ItemStack): CustomShardItems? {
            return map[itemStack]
        }
    }

    // Flag must be unique for each custom item
    protected abstract val flag: Int
    abstract val itemStack: ItemStack
}