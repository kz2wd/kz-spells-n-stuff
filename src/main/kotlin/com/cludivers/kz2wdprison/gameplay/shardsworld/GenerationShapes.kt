package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.gameplay.utils.Utils.debug
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.regions.Region

enum class GenerationShapes {

    TORUS {
        override fun getExpression(seed: Int): String {
            return "-((sqrt(x*x*20+z*z*20)-1.8)^2+y*y*20-1.5)+x"
        }
//        override val domainStart: Vector3 = Vector3.at(-4.0, -1.0, -4.0)
//        override val domainEnd: Vector3 = Vector3.at(4.0, -1.0, 4.0)
    },
    GRID {
        override fun getExpression(seed: Int): String {
            return "-(cos(x)+cos(y)+cos(z)+(51/100)*(cos(x)*cos(y)+cos(y)*cos(z)+cos(z)*cos(x))+(147/100))"
        }
    },

    ;

    abstract fun getExpression(seed: Int): String
    fun getUnit(region: Region): Vector3 {
        val min = region.minimumPoint.toVector3()
        val max = region.maximumPoint.toVector3()
        val unit = max.subtract(min)
        return unit
    }

    fun getOrigin(region: Region): Vector3 {
        val min = region.minimumPoint.toVector3()
        val max = region.maximumPoint.toVector3()
        val origin = max.add(min).divide(2.0)
        return origin
    }

    fun getGeneration(region: Region, pattern: Pattern, hollow: Boolean, seed: Int): (EditSession) -> Unit {
        return generateShape(region, pattern, getExpression(seed), getUnit(region), getOrigin(region), hollow)
    }

    open val domainStart: Vector3 = Vector3.at(-1.0, -1.0, -1.0)
    open val domainEnd: Vector3 = Vector3.at(1.0, 1.0, 1.0)

    companion object {
        fun generateShape(
            region: Region,
            pattern: Pattern,
            expression: String,
            unitVector: Vector3,
            origin: Vector3,
            hollow: Boolean
        ): (EditSession) -> Unit {
            // Made with the help of :
            // https://github.com/EngineHub/WorldEdit/blob/master/worldedit-core/src/main/java/com/sk89q/worldedit/command/GenerationCommands.java#L362

            return { editSession ->
                debug("Generating shape")
                editSession.makeShape(region, origin, unitVector, pattern, expression, hollow)
            }
        }
    }
}