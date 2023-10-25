package com.cludivers.kz2wdprison.gameplay.world.cuboid

import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

class Cuboid(loc1: Location, loc2: Location) {

    val start = Location(loc1.world, min(loc1.x, loc2.x), min(loc1.y, loc2.y), min(loc1.z, loc2.z)).toBlockLocation()
    val end: Location = Location(loc1.world, max(loc1.x, loc2.x), max(loc1.y, loc2.y), max(loc1.z, loc2.z)).toBlockLocation()

    private val size: Int = ((end.x - start.x) * (end.y - start.y) * (end.z - start.z)).toInt()

    fun goOver(): Iterator<Location>{
        return iterator {
            for (x in start.x.toInt()..end.x.toInt()){
                for (y in start.y.toInt()..end.y.toInt()){
                    for (z in start.z.toInt()..end.z.toInt()){
                        yield(Location(start.world, x.toDouble(), y.toDouble(), z.toDouble()))
                    }
                }
            }
        }
    }

    fun fillWithPattern(pattern: BlockPattern){
        val iter = goOver()
        while (iter.hasNext()){
            iter.next().block.type = pattern.getMaterial()
        }
    }

    companion object {
        fun Player.isInArea(cuboid: Cuboid): Boolean {
            return cuboid.start.x <= this.location.x && this.location.x <= cuboid.end.x &&
                    cuboid.start.y <= this.location.y && this.location.y <= cuboid.end.y &&
                    cuboid.start.z <= this.location.z && this.location.z <= cuboid.end.z
        }
    }
}