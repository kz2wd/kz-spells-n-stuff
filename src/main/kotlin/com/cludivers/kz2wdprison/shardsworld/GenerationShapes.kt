package com.cludivers.kz2wdprison.shardsworld

import com.cludivers.kz2wdprison.utils.Utils.debug
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.function.pattern.Pattern
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.regions.Region

enum class GenerationShapes {

    TORUS {
        override fun getExpression(seed: Int): String {
            return "-((sqrt(x*x+z*z)-3)^2+y*y-1)"
        }

        override val domainScale: Vector3 = Vector3.at(10.0, 10.0, 10.0)
    },
    GRID {
        override fun getExpression(seed: Int): String {
            return "-(cos(x)+cos(y)+cos(z)+(51/100)*(cos(x)*cos(y)+cos(y)*cos(z)+cos(z)*cos(x))+(147/100))"
        }
    },
    ORTHOCIRCLE {
        override fun getExpression(seed: Int): String {
            return "-(((x^2+y^2-1)^2+z^2)*((y^2+z^2-1)^2+x^2)*((z^2+x^2-1)^2+y^2)-(3/40)^2*(1+3*(x^2+y^2+z^2)))"
        }

        override val domainScale: Vector3 = Vector3.at(3.0, 3.0, 3.0)
    },

    TREFOIL_KNOT {
        override fun getExpression(seed: Int): String {

            return "(-8*(x^2+z^2)^2*(x^2+z^2+1+y^2+0.3^2-0.2^2)+0.36*(x^3-3*x*z^2)*y^2+0.36*(2*(x^2+z^2)^2-" +
                    "(x^3-3*x*z^2)*(x^2+z^2+1))+0.72*(3*x^2*z-z^3)*y)^2-(x^2+z^2)*(2*(x^2+z^2)*(x^2+z^2+1+y^2+0.05)" +
                    "^2+8*(x^2+z^2)^2+0.36*(2*(x^3-3*x*z^2)-(x^2+z^2)*(x^2+z^2+1))-0.72*(3*x^2*z-z^3)*y-4*(x^2+z^2)" +
                    "*0.09*y^2)^2"
        }

        override val domainScale: Vector3 = Vector3.at(4.0, 4.0, 4.0)
    },
    HYPERBOLOID {
        override fun getExpression(seed: Int): String {
            return "-(x^2+z^2-y^2-3/10)"
        }

        override val domainScale: Vector3 = Vector3.at(4.0, 2.0, 4.0)
    }



    ;

    abstract fun getExpression(seed: Int): String
    fun getUnit(region: Region): Vector3 {
        val min = region.minimumPoint.toVector3()
        val max = region.maximumPoint.toVector3()
        val unit = max.subtract(min)
        return debug(unit.divide(domainScale))
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

    open val domainScale: Vector3 = Vector3.at(1.0, 1.0, 1.0)

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