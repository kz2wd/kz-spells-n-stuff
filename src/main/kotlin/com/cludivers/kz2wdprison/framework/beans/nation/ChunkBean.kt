package com.cludivers.kz2wdprison.framework.beans.nation

import jakarta.persistence.*
import org.bukkit.Chunk
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
    var nation: NationBean? = null

    companion object {

        fun getChunkBean(session: Session, chunk: Chunk): ChunkBean{
            return getChunkBean(session, chunk.x, chunk.z)
        }
        fun getChunkBean(session: Session, x: Int, z: Int): ChunkBean {
            val chunk = session
                .createQuery("from ChunkBean C where C.xCoord = :x AND C.zCoord = :z", ChunkBean::class.java)
                .setParameter("x", x)
                .setParameter("z", z)
                .uniqueResult()

            if (chunk is ChunkBean){
                return chunk
            }

            // Add the chunk
            session.beginTransaction()
            val chunkToAdd = ChunkBean()
            chunkToAdd.xCoord = x
            chunkToAdd.zCoord = z
            session.persist(chunkToAdd)
            session.transaction.commit()
            return chunkToAdd
        }
    }
}