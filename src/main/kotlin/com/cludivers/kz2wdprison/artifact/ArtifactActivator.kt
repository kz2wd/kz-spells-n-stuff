package com.cludivers.kz2wdprison.artifact

import com.cludivers.kz2wdprison.persistence.beans.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.persistence.beans.player.PlayerSkills
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

interface ArtifactActivator {
    fun getLocation(): Location
    fun getSelf(): Entity

    fun getCasterLevel(): Int

    fun getAttacker(): Entity?

    fun getAttacked(): Entity?

    // Return a player to check for permissions on his rights, if null, act as if it has no permissions
    fun getPermission(): Player?

    companion object {
        fun playerToCaster(player: Player, attacker: Entity? = null, attacked: Entity? = null): ArtifactActivator {
            return object : ArtifactActivator {
                override fun getLocation(): Location {
                    return player.location
                }

                override fun getSelf(): Entity {
                    return player.player as Entity
                }

                override fun getCasterLevel(): Int {
                    return player.getData().intrinsic.skills[PlayerSkills.ARTIFACT_MASTERY] ?: 0
                }

                override fun getAttacker(): Entity? {
                    return attacker
                }

                override fun getAttacked(): Entity? {
                    return attacked
                }

                override fun getPermission(): Player {
                    return player
                }
            }
        }
    }
}
