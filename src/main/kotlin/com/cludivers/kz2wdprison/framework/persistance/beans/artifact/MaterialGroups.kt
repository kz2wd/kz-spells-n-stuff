package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class MaterialGroups {
    Blocks,
    Projectiles,
    PotionMaterial,
    Custom,
    None
    ;

    companion object {
        fun getMaterialGroup(itemStack: ItemStack): MaterialGroups {
            if (CustomShardItems.getCustomItemStack(itemStack) != null){
                return Custom
            }

            return when(itemStack.type){
                Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE, Material.DEEPSLATE_BRICKS, Material.STONE_BRICKS -> Blocks
                Material.SUGAR -> PotionMaterial
                Material.ARROW, Material.EGG, Material.SNOWBALL -> Projectiles
                else -> None
            }
        }
    }

}