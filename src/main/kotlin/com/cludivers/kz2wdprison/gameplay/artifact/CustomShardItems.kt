package com.cludivers.kz2wdprison.gameplay.artifact

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomShardItems {
    SHARDS {
        override val flag: Int = 10000

        override val itemStack: ItemStack = run {
            buildItemStack(Material.IRON_NUGGET, flag)
        }

    },
    PROJECTILE_SPEC {
        override val flag: Int = 20001

        override val itemStack: ItemStack = run {
            buildItemStack(Material.IRON_NUGGET, flag)
        }
    },
    BLOCK_SPEC {
        override val flag: Int = 20002

        override val itemStack: ItemStack = run {
            buildItemStack(Material.IRON_NUGGET, flag)
        }
    }
    ;

    protected fun buildItemStack(material: Material, flag: Int): ItemStack{
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.setCustomModelData(flag)
        item.setItemMeta(meta)
        return item
    }

    // Flag must be unique for each custom item
    protected abstract val flag: Int
    abstract val itemStack : ItemStack
}