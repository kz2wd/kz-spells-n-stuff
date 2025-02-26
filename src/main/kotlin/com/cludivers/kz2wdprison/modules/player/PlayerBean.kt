package com.cludivers.kz2wdprison.modules.player

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.nation.beans.NationBean
import com.cludivers.kz2wdprison.modules.nation.beans.PermissionGroup
import jakarta.persistence.*
import org.bukkit.entity.Player
import org.hibernate.exception.ConstraintViolationException

@Entity
class PlayerBean {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false, unique = true)
    var uuid: String? = null

    var connectionAmount: Int = 0

    var shards: Double = 1.0
    var shardsEfficiencyFactor: Double = 1.0

    @Embedded
    var intrinsic: PlayerIntrinsic = PlayerIntrinsic()

    @OneToOne
    var nation: NationBean? = null

    @ManyToOne
    var permissionGroup: PermissionGroup? = null

    companion object {
        private fun getPlayerPlayerBean(player: Player): PlayerBean {

            fun playerBeanRequest(): PlayerBean? {
                return PluginConfiguration.session
                    .createQuery("from PlayerBean P where P.uuid = :uuid", PlayerBean::class.java)
                    .setParameter("uuid", player.uniqueId.toString())
                    .uniqueResult()
            }

            var playerData = playerBeanRequest()

            if (playerData == null) {
                val transaction = PluginConfiguration.session.beginTransaction()
                playerData = PlayerBean()
                playerData.uuid = player.uniqueId.toString()
                // In case of race condition !
                try {
                    PluginConfiguration.session.persist(playerData)
                    transaction.commit()
                } catch (e: ConstraintViolationException) {
                    transaction.rollback()
                    playerData = playerBeanRequest()
                }

            }
            return playerData
        }

        fun Player.getData(): PlayerBean {
            return getPlayerPlayerBean(this)
        }
    }
}
