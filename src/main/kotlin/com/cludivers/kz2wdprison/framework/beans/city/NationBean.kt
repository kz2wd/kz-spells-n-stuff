package com.cludivers.kz2wdprison.framework.beans.city

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.sql.Date

@Entity
class NationBean {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var name: String? = null

    // Owner
    @OneToOne
    var owner: PlayerBean? = null

    var creationDate: Date? = null

    @OneToMany
    var residents: List<PermissionGroup>? = null

    @ManyToMany
    var criminals: List<PlayerBean>? = null

    @OneToMany
    var chunks: List<ChunkBean>? = null

    @OneToMany
    var plots: List<NationPlot>? = null

    @OneToOne
    var defaultAreaRules: AreaPermissionBean? = null

    var level: Int? = 0

    var chunkClaimTokens: Int? = 1

}