package com.cludivers.kz2wdprison.framework.configuration

import com.cludivers.kz2wdprison.framework.configuration.database.LocalDatabase
import com.cludivers.kz2wdprison.framework.configuration.database.RemoteDatabase
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.*
import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import org.bukkit.configuration.file.FileConfiguration
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources

class HibernateConfigurationHandler {
    companion object {
        fun loadSessionFactory(config: FileConfiguration): SessionFactory {

            val classes = listOf(
                PlayerBean::class.java,
                AreaPermission::class.java,
                ChunkBean::class.java,
                NationBean::class.java,
                NationPermission::class.java,
                NationPlot::class.java,
                PermissionGroup::class.java,
                Artifact::class.java,
                ArtifactComplexRune::class.java,
            )

            val serviceRegistryBuilder = if (PluginConfiguration.isDatabaseConnected) {
                RemoteDatabase.loadRemoteDatabase(config)
            } else {
                LocalDatabase.loadLocalDatabase()
            }


            val meta = MetadataSources(serviceRegistryBuilder.build()).apply {
                classes.forEach {
                    addAnnotatedClass(it)
                }
            }

            return meta.buildMetadata().buildSessionFactory()
        }
    }
}