package com.cludivers.kz2wdprison.framework.beans.artifact

import com.cludivers.kz2wdprison.gameplay.artifact.Consumer
import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.Producer
import com.cludivers.kz2wdprison.gameplay.attributes.PlayerAttribute
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

enum class Specifications: Consumer, Producer {

    BLOCKS{
        override fun produce(session: Session, item: ItemStack, entity: Entity, location: Location): Float {
            if (item === CustomShardItems.SHARDS.itemStack){
                if (entity is Player){
                    val playerData = entity.getData(session)
                    return playerData.shards * playerData.shardsEfficiencyFactor
                }
            }
            return 0f
        }
    },
    PROJECTILE{
        override fun consume(session: Session, item: ItemStack, entity: Entity, location: Location, flow: Float) {

            if (flow <= 0){
                return
            }
            if (item.type === Material.ARROW){
                location.world.spawnArrow(location.add(location.direction), location.direction, flow + 1, 1 / (flow + 1))
            }
        }
    };
    override fun consume(session: Session, item: ItemStack, entity: Entity, location: Location, flow: Float) {
        // Default behavior : do nothing
    }

    override fun produce(session: Session, item: ItemStack, entity: Entity, location: Location): Float {
        // Default behavior : do nothing
        return 0f
    }

}