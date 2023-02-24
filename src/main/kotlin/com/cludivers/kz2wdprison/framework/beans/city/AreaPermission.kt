package com.cludivers.kz2wdprison.framework.beans.city

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class AreaPermission {
    @Id
    @GeneratedValue
    var id: Long? = null

    var canBreak: Boolean = false
    var canPlace: Boolean = false
    var canAttackPlayer: Boolean = false
}