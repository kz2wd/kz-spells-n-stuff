package com.cludivers.kz2wdprison.gameplay.nation.beans

import com.cludivers.kz2wdprison.framework.persistence.beans.player.PlayerBean
import jakarta.persistence.*

@Entity
class NationAttack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToOne
    var targetNation: NationBean? = null

    @ManyToOne
    var attackingPlayer: PlayerBean? = null

    var shardAmount: Int = 0

    var attackTier: ProtectionTiers? = null

}