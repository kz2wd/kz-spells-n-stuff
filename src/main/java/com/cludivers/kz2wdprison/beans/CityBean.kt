package com.cludivers.kz2wdprison.beans

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class CityBean {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    var id: Long? = null

    // Owner


    // Chunks


    // Permissions

    // Grades

    // Level

    // Name

}