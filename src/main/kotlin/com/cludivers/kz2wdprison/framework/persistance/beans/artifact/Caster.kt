package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

interface Caster {
    fun getLocation(): Location
    fun getSelf(): Entity

    /**
     * Returns a block of AIR in the sight of the caster
     */
    fun getSightAirBlock(maxDistance: Int): Block?
    fun getSightBlock(maxDistance: Int): Block?
    fun getSightEntity(maxDistance: Int): Entity?

    companion object {
        fun playerToCaster(player: Player): Caster{
            return object : Caster {
                override fun getLocation(): Location {
                    return player.location
                }

                override fun getSelf(): Entity {
                    return player.player as Entity
                }

                override fun getSightAirBlock(maxDistance: Int): Block? {
                    return player.getLastTwoTargetBlocks(null, maxDistance)[0]
                }

                override fun getSightBlock(maxDistance: Int): Block? {
                    return player.getTargetBlockExact(maxDistance)
                }

                override fun getSightEntity(maxDistance: Int): Entity? {
                    return player.getTargetEntity(maxDistance)
                }
            }
        }
    }
}
