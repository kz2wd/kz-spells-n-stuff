package com.cludivers.kz2wdprison

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.bukkit.entity.Player
import org.hibernate.Session

@Entity
class PlayerBean {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    var uuid: String? = null

    var connectionAmount: Int = 0

    var level: Int = 0
    var currentXp = 0
    var skillPoint = 0

    var healthLevel = 0
    var foodLevel = 0
    var miningLevel = 0
    var pickaxeLevel = 0

    companion object {
        fun getPlayerInfo(player: Player, session: Session): PlayerBean {
            var playerData = session
                    .createQuery("from PlayerBean P where P.uuid = :uuid", PlayerBean::class.java)
                    .setParameter("uuid", player.uniqueId.toString())
                    .uniqueResult()

            if (playerData == null){
                playerData = PlayerBean()
                playerData.uuid = player.uniqueId.toString();
                session.persist(playerData)
            }

            return playerData
        }
    }
}