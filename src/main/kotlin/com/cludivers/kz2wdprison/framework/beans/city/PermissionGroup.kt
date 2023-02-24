package com.cludivers.kz2wdprison.framework.beans.city

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class PermissionGroup {
    @Id
    @GeneratedValue
    var id: Long? = null

    var name: String? = null

    @OneToOne
    var areaPermission: AreaPermission? = null

    @OneToOne
    var cityPermission: CityPermission? = null



}