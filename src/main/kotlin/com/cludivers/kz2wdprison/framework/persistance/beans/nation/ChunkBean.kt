package com.cludivers.kz2wdprison.framework.persistance.beans.nation

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import jakarta.persistence.*
import org.bukkit.Chunk

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

        fun getChunkBean(chunk: Chunk): ChunkBean {
            return getChunkBean(chunk.x, chunk.z)
        }

        fun getChunkBean(x: Int, z: Int): ChunkBean {
            val chunk = PluginConfiguration.session
                .createQuery("from ChunkBean C where C.xCoord = :x AND C.zCoord = :z", ChunkBean::class.java)
                .setParameter("x", x)
                .setParameter("z", z)
                .uniqueResult()

            if (chunk is ChunkBean) {
                return chunk
            }

            // Add the chunk
            PluginConfiguration.session.beginTransaction()
            val chunkToAdd = ChunkBean()
            chunkToAdd.xCoord = x
            chunkToAdd.zCoord = z
            PluginConfiguration.session.persist(chunkToAdd)
            PluginConfiguration.session.transaction.commit()
            return chunkToAdd
        }
    }
}