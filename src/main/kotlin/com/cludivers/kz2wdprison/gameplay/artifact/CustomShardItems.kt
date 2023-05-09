package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.utils.Utils.buildItemStack
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val flag: Int = 10000
        override val itemStack : ItemStack = run {
            buildItemStack(Component.text("Shards"), Material.IRON_NUGGET, flag)
        }
    },
    EMPTY_SPEC {
        override val flag: Int = 20000
        override val itemStack : ItemStack = run {
            buildItemStack(Component.text("Empty"), Material.IRON_NUGGET, flag)
        }
    },
    PROJECTILE_SPEC {
        override val flag: Int = 20001
        override val itemStack : ItemStack = run {
            buildItemStack(Component.text("Projectile"), Material.IRON_NUGGET, flag)
        }
    },
    BLOCK_SPEC {
        override val flag: Int = 20002
        override val itemStack : ItemStack = run {
            buildItemStack(Component.text("Block"), Material.IRON_NUGGET, flag)
        }
    },
    PLAYER_EFFECT_SPEC {
        override val flag: Int = 20003
        override val itemStack : ItemStack = run {
            buildItemStack(Component.text("Self effect"), Material.IRON_NUGGET, flag)
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