package com.cludivers.kz2wdprison.gameplay.artifact

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val flag: Int = 10000

        override val itemStack: ItemStack = run {
            val item = ItemStack(Material.IRON_NUGGET)
            val meta = item.itemMeta
            meta.setCustomModelData(flag)
            item.setItemMeta(meta)
            item
        }

    };

    // Flag must be unique for each custom item
    protected abstract val flag: Int
    abstract val itemStack : ItemStack
}