package com.cludivers.kz2wdprison.framework.beans

import com.cludivers.kz2wdprison.framework.beans.nation.NationBean
import com.cludivers.kz2wdprison.framework.beans.nation.PermissionGroup
import com.cludivers.kz2wdprison.framework.beans.ores.OresMinedStatistics
import com.cludivers.kz2wdprison.gameplay.player.getData
import jakarta.persistence.*
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
    var currentXp: Float = 0f
    var skillPoint = 0

    var healthLevel = 0
    var miningLevel = 0
    var pickaxeLevel = 0
    var criticOddLevel: Int = 0
    var criticFactorLevel: Int = 0

    @Embedded
    var oresStats: OresMinedStatistics =
        OresMinedStatistics()

    @OneToOne
    var nation: NationBean? = null

    @ManyToMany
    @JoinTable(name = "player_groups",
        joinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name="player_id", referencedColumnName = "id")])
    var groups: List<PermissionGroup>? = null

    companion object {
        fun getPlayerPlayerBean(session: Session, player: Player): PlayerBean{
            var playerData = session
                .createQuery("from PlayerBean P where P.uuid = :uuid", PlayerBean::class.java)
                .setParameter("uuid", player.uniqueId.toString())
                .uniqueResult()

            if (playerData == null){
                playerData = PlayerBean()
                playerData.uuid = player.uniqueId.toString()
                session.persist(playerData)
            }

            return playerData
        }
    }
}
