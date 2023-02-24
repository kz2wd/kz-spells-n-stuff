package com.cludivers.kz2wdprison.framework.beans.city

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class NationPermission {
    @Id
    @GeneratedValue
    var id: Long? = null

    var rankAuthority: Int = 0

    var canInvite: Boolean = false
    var canBan: Boolean = false
    var canManagePermission = false
    var canSetCriminal: Boolean = false
}