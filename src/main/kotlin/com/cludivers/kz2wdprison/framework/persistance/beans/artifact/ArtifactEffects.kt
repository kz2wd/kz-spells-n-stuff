package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class ArtifactEffects {
    BLOCKS {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput) {
            input.locations.forEach { it.world.getBlockAt(it).type = itemStack.type }
        }
    },
    PROJECTILES {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput) {
            if (input.vector == null) {
                return
            }
            if (itemStack.type == Material.ARROW) {
                input.locations.forEach { it.world.spawnArrow(it, input.vector!!, 1f, 1f) }
            }
        }
    },
    CONSUMABLE {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput) {
            input.entities.forEach { foodEffect(it, itemStack.type) }
        }
    },
    TOOLS {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput) {
            input.locations.forEach {it.world.getBlockAt(it).breakNaturally(itemStack) }
        }
    },
    ENTITY {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput) {

        }
    },
    Custom, None;

    companion object {
        fun getMaterialGroup(itemStack: ItemStack): ArtifactEffects {
            if (CustomShardItems.getCustomItemStack(itemStack) != null) {
                return Custom
            }

            return when (itemStack.type) {
                Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE, Material.DEEPSLATE_BRICKS, Material.STONE_BRICKS -> BLOCKS
                Material.ARROW -> PROJECTILES
                Material.APPLE, Material.BAKED_POTATO, Material.BEETROOT, Material.BEETROOT_SOUP, Material.BREAD,
                Material.CAKE, Material.CARROT, Material.CHORUS_FRUIT, Material.COOKED_CHICKEN, Material.COOKED_COD,
                Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT, Material.COOKED_SALMON,
                Material.COOKIE, Material.DRIED_KELP, Material.ENCHANTED_GOLDEN_APPLE, Material.GOLDEN_APPLE,
                Material.GLOW_BERRIES, Material.GOLDEN_CARROT, Material.HONEY_BOTTLE, Material.MELON_SLICE,
                Material.MUSHROOM_STEW, Material.POISONOUS_POTATO, Material.POTATO, Material.PUFFERFISH,
                Material.PUMPKIN_PIE, Material.RABBIT_STEW, Material.BEEF, Material.CHICKEN, Material.COD,
                Material.MUTTON, Material.PORKCHOP, Material.RABBIT, Material.SALMON, Material.ROTTEN_FLESH,
                Material.SPIDER_EYE, Material.COOKED_BEEF, Material.SUSPICIOUS_STEW, Material.SWEET_BERRIES,
                Material.TROPICAL_FISH -> CONSUMABLE
                Material.DIAMOND_PICKAXE -> TOOLS
                else -> None
            }
        }
    }

    fun foodEffect(entity: Entity, material: Material) {
        when (material) {
            Material.APPLE -> feedPlayer((entity as Player), 2.4f, 4)
            Material.BAKED_POTATO -> feedPlayer((entity as Player), 6f, 5)
            Material.BEETROOT -> feedPlayer((entity as Player), 1.2f, 1)
            Material.BEETROOT_SOUP -> feedPlayer((entity as Player), 7.2f, 6)
            Material.BREAD -> feedPlayer((entity as Player), 5f, 6)
            Material.CAKE -> feedPlayer((entity as Player), 0.4f, 2)
            Material.CARROT -> feedPlayer((entity as Player), 3.6f, 3)
            Material.CHORUS_FRUIT -> feedPlayer((entity as Player), 2.4f, 4)
            Material.COOKED_CHICKEN -> feedPlayer((entity as Player), 7.2f, 6)
            Material.COOKED_COD -> feedPlayer((entity as Player), 6f, 5)
            Material.COOKED_MUTTON -> feedPlayer((entity as Player), 9.6f, 6)
            Material.COOKED_PORKCHOP -> feedPlayer((entity as Player), 12.8f, 8)
            Material.COOKED_RABBIT -> feedPlayer((entity as Player), 6f, 5)
            Material.COOKED_SALMON -> feedPlayer((entity as Player), 9.6f, 6)
            Material.COOKIE -> feedPlayer((entity as Player), 0.4f, 2)
            Material.DRIED_KELP -> feedPlayer((entity as Player), 0.6f, 1)
            Material.ENCHANTED_GOLDEN_APPLE -> feedPlayer((entity as Player), 9.6f, 4)
            Material.GOLDEN_APPLE -> feedPlayer((entity as Player), 9.6f, 4)
            Material.GLOW_BERRIES -> feedPlayer((entity as Player), 0.4f, 2)
            Material.GOLDEN_CARROT -> feedPlayer((entity as Player), 14.4f, 6)
            Material.HONEY_BOTTLE -> feedPlayer((entity as Player), 1.2f, 6)
            Material.MELON_SLICE -> feedPlayer((entity as Player), 1.2f, 2)
            Material.MUSHROOM_STEW -> feedPlayer((entity as Player), 7.2f, 6)
            Material.POISONOUS_POTATO -> feedPlayer((entity as Player), 1.2f, 2)
            Material.POTATO -> feedPlayer((entity as Player), 0.6f, 1)
            Material.PUFFERFISH -> feedPlayer((entity as Player), 0.2f, 1)
            Material.PUMPKIN_PIE -> feedPlayer((entity as Player), 4.8f, 8)
            Material.RABBIT_STEW -> feedPlayer((entity as Player), 12f, 10)
            Material.BEEF -> feedPlayer((entity as Player), 1.8f, 3)
            Material.CHICKEN -> feedPlayer((entity as Player), 1.2f, 2)
            Material.COD -> feedPlayer((entity as Player), 0.4f, 2)
            Material.MUTTON -> feedPlayer((entity as Player), 1.2f, 2)
            Material.PORKCHOP -> feedPlayer((entity as Player), 1.8f, 3)
            Material.RABBIT -> feedPlayer((entity as Player), 1.8f, 3)
            Material.SALMON -> feedPlayer((entity as Player), 0.4f, 2)
            Material.ROTTEN_FLESH -> feedPlayer((entity as Player), 0.8f, 4)
            Material.SPIDER_EYE -> feedPlayer((entity as Player), 3.2f, 2)
            Material.COOKED_BEEF -> feedPlayer((entity as Player), 12.8f, 8)
            Material.SUSPICIOUS_STEW -> feedPlayer((entity as Player), 7.2f, 6)
            Material.SWEET_BERRIES -> feedPlayer((entity as Player), 0.4f, 2)
            Material.TROPICAL_FISH -> feedPlayer((entity as Player), 0.2f, 1)
            else -> {}
        }
    }

    private fun feedPlayer(player: Player, saturationDelta: Float, foodDelta: Int) {
        player.saturation += saturationDelta
        player.foodLevel += foodDelta
    }

    open fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput) {

    }

}