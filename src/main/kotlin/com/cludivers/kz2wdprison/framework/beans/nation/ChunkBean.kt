package com.cludivers.kz2wdprison.framework.beans.nation

import jakarta.persistence.*

@Entity
class ChunkBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var yCoord: Int? = null

    var xCoord: Int? = null

    @ManyToOne
    var city: NationBean? = null
}