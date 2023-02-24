package com.cludivers.kz2wdprison.framework.beans

import com.cludivers.kz2wdprison.framework.beans.city.NationBean
import com.cludivers.kz2wdprison.framework.beans.city.PermissionGroup
import com.cludivers.kz2wdprison.framework.beans.ores.OresMinedStatistics
import jakarta.persistence.*

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
}