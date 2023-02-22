package com.cludivers.kz2wdprison.gameplay.player

import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player

class PlayerTransientData {

    companion object {

        private val playersTransientData: HashMap<Player, PlayerTransientData> = hashMapOf()

        fun getPlayerTransientData(player: Player): PlayerTransientData {
            val data = playersTransientData[player]
            if (data !is PlayerTransientData){
                val newData = PlayerTransientData()
                playersTransientData[player] = newData
                return newData
            }
            return data
        }
    }

    var currentBossBarDisplay: BossBar? = null
}