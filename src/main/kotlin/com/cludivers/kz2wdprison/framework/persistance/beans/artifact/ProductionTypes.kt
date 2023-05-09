package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.Producer
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hibernate.Session

enum class ProductionTypes {
    EMPTY {
        override val customItemStack: CustomShardItems = CustomShardItems.EMPTY_SPEC
    },
    PLAYER_ATTRIBUTE {
        override fun produce(session: Session, item: ItemStack, entity: Entity, location: Location, level: Int): Float {
            if (item == CustomShardItems.SHARDS.itemStack){
                if (entity is Player){
                    val playerData = entity.getData(session)
                    return playerData.shards * playerData.shardsEfficiencyFactor
                }
            }
            return 0f
        }

        override val customItemStack: CustomShardItems = CustomShardItems.BLOCK_SPEC
    },
    SELF_EFFECT {
        override val customItemStack: CustomShardItems = CustomShardItems.PLAYER_EFFECT_SPEC
        override fun produce(session: Session, item: ItemStack, entity: Entity, location: Location, level: Int): Float {
            return when(item.type){
                Material.SOUL_SAND -> {
                    (entity as Player).addPotionEffect(PotionEffect(PotionEffectType.SLOW, level, level))
                    level * 3f
                }
                else -> {0f}
            }
        }
    };

    open fun produce(session: Session, item: ItemStack, entity: Entity, location: Location, level: Int): Float {
        // Default behavior : do nothing
        return 0f
    }

    companion object {
        private val map = ProductionTypes.values().associateBy(ProductionTypes::customItemStack)
        fun itemStackToProducerType(itemStack: ItemStack) : ProductionTypes {
            return ProductionTypes.map[CustomShardItems.getCustomItemStack(itemStack)] ?: EMPTY
        }
    }

    abstract val customItemStack: CustomShardItems
}