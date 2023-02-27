package com.cludivers.kz2wdprison.framework.beans.city

import jakarta.persistence.*
import org.hibernate.Session

@Entity
class ChunkBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var xCoord: Int? = null

    var zCoord: Int? = null

    @ManyToOne
    var city: NationBean? = null

    companion object {
        fun getChunkBean(session: Session, x: Int, z: Int): ChunkBean? {
            return session
                .createQuery("from ChunkBean C where C.xCoord = :x AND C.zCoord = :z", ChunkBean::class.java)
                .setParameter("x", x)
                .setParameter("z", z)
                .uniqueResult()
        }
    }
}