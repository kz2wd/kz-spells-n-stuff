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
    var group_id: Long? = null

    var name: String? = null

    @OneToOne
    var areaPermission: AreaPermission? = null

    @OneToOne
    var cityPermission: CityPermission? = null

    @ManyToMany(mappedBy = "groups")
    var players: List<PlayerBean>? = null

}