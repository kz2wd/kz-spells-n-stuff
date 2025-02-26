package com.cludivers.kz2wdprison.framework.configuration

import com.cludivers.kz2wdprison.framework.configuration.database.LocalDatabase
import com.cludivers.kz2wdprison.framework.configuration.database.RemoteDatabase
import com.cludivers.kz2wdprison.modules.nation.beans.AreaPermission
import com.cludivers.kz2wdprison.modules.nation.beans.ChunkBean
import com.cludivers.kz2wdprison.modules.nation.beans.NationBean
import com.cludivers.kz2wdprison.modules.nation.beans.NationPlot
import com.cludivers.kz2wdprison.modules.nation.beans.PermissionGroup
import com.cludivers.kz2wdprison.modules.nation.beans.NationAttack
import com.cludivers.kz2wdprison.modules.player.PlayerBean
import com.cludivers.kz2wdprison.modules.artifact.Artifact
import com.cludivers.kz2wdprison.modules.shardsworld.PlotState
import com.cludivers.kz2wdprison.modules.shardsworld.rules.PlotRules
import org.bukkit.configuration.file.FileConfiguration
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources

class HibernateConfigurationHandler {
    companion object {
        fun loadSessionFactory(config: FileConfiguration?): SessionFactory {

            val classes = listOf(
                NationBean::class.java,
                PlayerBean::class.java,
                AreaPermission::class.java,
                ChunkBean::class.java,
                com.cludivers.kz2wdprison.modules.nation.beans.NationPermission::class.java,
                NationPlot::class.java,
                PermissionGroup::class.java,
                Artifact::class.java,
                NationAttack::class.java,
                PlotRules::class.java,
                PlotState::class.java,
            )

            val serviceRegistryBuilder = if (config != null && PluginConfiguration.isDatabaseConnected) {
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