package com.cludivers.kz2wdprison.framework.configuration

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.*
import com.cludivers.kz2wdprison.framework.persistance.beans.player.AttributeItem
import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import org.bukkit.configuration.file.FileConfiguration
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.service.ServiceRegistry

class HibernateConfigurationHandler {
    companion object {
        fun loadHibernateConfiguration(config: FileConfiguration): SessionFactory {
            val configuration = Configuration()

            val login = config["login"]
            val password = config["password"]
            val hbmMode = config["hbm"]

            configuration.setProperty("dialect", "MySQL")
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/server_db")
            configuration.setProperty("hibernate.connection.username", login.toString())
            configuration.setProperty("hibernate.connection.password", password.toString())
            configuration.setProperty("hibernate.hbm2ddl.auto", hbmMode.toString())
            configuration.setProperty("show_sql", "true") // Not working :(

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
                AttributeItem::class.java,
            )

            classes.forEach {
                configuration.addAnnotatedClass(it)
            }

            val serviceRegistry: ServiceRegistry =
                StandardServiceRegistryBuilder().applySettings(configuration.properties).build()

            return configuration.buildSessionFactory(serviceRegistry)
        }
    }
}