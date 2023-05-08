package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

enum class ConsumptionTypes {
    EMPTY {
        override val customItemStack: CustomShardItems = CustomShardItems.EMPTY_SPEC
    },
    PROJECTILE{
        override fun consume(session: Session, item: ItemStack, entity: Entity, location: Location, flow: Float) {

            if (flow <= 0){
                return
            }
            when(item.type){
                Material.ARROW -> location.world.spawnArrow(location.add(location.direction), location.direction, flow / 100 + 1, 1 / (flow / 200 + 1))
                Material.SNOWBALL -> {
                    val snowball = location.world.spawnEntity(location.add(location.direction), EntityType.SNOWBALL)
                    snowball.velocity = location.direction
                }

                else -> {}
            }
        }
        override val customItemStack: CustomShardItems = CustomShardItems.PROJECTILE_SPEC
    };

    open fun consume(session: Session, item: ItemStack, entity: Entity, location: Location, flow: Float) {
        // Default behavior : do nothing
    }

    companion object {
        private val map = ConsumptionTypes.values().associateBy(ConsumptionTypes::customItemStack)
        fun itemStackToConsumerType(itemStack: ItemStack): ConsumptionTypes {
            return map[CustomShardItems.getCustomItemStack(itemStack)] ?: EMPTY
        }
    }

    abstract val customItemStack: CustomShardItems
}