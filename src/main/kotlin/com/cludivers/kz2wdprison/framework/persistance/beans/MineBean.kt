package com.cludivers.kz2wdprison.framework.persistance.beans

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class MineBean {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    var id: Long? = null

    // Owner

    // Size

    // Location

    // Materials

}