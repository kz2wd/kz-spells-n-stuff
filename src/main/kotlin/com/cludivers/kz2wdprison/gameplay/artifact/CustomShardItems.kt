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