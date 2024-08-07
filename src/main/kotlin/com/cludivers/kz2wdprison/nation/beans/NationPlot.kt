package com.cludivers.kz2wdprison.nation.beans

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class NationPlot {
    @Id
    @GeneratedValue
    var id: Long? = null

    @OneToMany
    var groups: List<PermissionGroup>? = null

}