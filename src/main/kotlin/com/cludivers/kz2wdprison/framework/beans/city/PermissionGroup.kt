package com.cludivers.kz2wdprison.framework.beans.city

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne

@Entity
class PermissionGroup {
    @Id
    @GeneratedValue
    var id: Long? = null

    var name: String? = null

    @OneToOne
    var areaPermissionBean: AreaPermissionBean? = null

    @OneToOne
    var nationPermission: NationPermission? = null

    @ManyToMany(mappedBy = "groups")
    var players: List<PlayerBean>? = null

}