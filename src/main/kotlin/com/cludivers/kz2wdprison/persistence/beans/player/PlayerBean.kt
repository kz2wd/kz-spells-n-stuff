package com.cludivers.kz2wdprison.persistence.beans.player

import com.cludivers.kz2wdprison.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.persistence.beans.ores.OresMinedStatistics
import com.cludivers.kz2wdprison.nation.beans.NationBean
import com.cludivers.kz2wdprison.nation.beans.PermissionGroup
import jakarta.persistence.*
import org.bukkit.entity.Player

@Entity
class PlayerBean {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false, unique = true)
    var uuid: String? = null

    var connectionAmount: Int = 0

    var shards: Int = 1
    var shardsEfficiencyFactor: Float = 1f

    @Embedded
    var intrinsic: PlayerIntrinsic = PlayerIntrinsic()

    @Embedded
    var oresStats: OresMinedStatistics =
        OresMinedStatistics()

    @OneToOne
    var nation: NationBean? = null

    @ManyToOne
    var permissionGroup: PermissionGroup? = null

    companion object {
        private fun getPlayerPlayerBean(player: Player): PlayerBean {
            var playerData = PluginConfiguration.session
                .createQuery("from PlayerBean P where P.uuid = :uuid", PlayerBean::class.java)
                .setParameter("uuid", player.uniqueId.toString())
                .uniqueResult()

            if (playerData == null) {
                playerData = PlayerBean()
                playerData.uuid = player.uniqueId.toString()
                PluginConfiguration.session.persist(playerData)
            }
            return playerData
        }

        fun Player.getData(): PlayerBean {
            return getPlayerPlayerBean(this)
        }
    }
}
