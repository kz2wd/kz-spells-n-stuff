package com.cludivers.kz2wdprison.framework.configuration.database

import com.cludivers.kz2wdprison.framework.configuration.ConfigurationFields
import org.bukkit.configuration.file.FileConfiguration
import org.hibernate.boot.registry.StandardServiceRegistryBuilder

object RemoteDatabase {
    fun loadRemoteDatabase(config: FileConfiguration): StandardServiceRegistryBuilder {

        val serviceRegistry = StandardServiceRegistryBuilder().apply {
            applySetting("dialect", "MySQL")
            applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
            applySetting(
                "hibernate.connection.url",
                "jdbc:mysql://localhost:${config[ConfigurationFields.DATABASE_PORT.fieldName]}/" +
                        "${config[ConfigurationFields.DATABASE_NAME.fieldName]}"
            )
            applySetting(
                "hibernate.connection.username",
                config[ConfigurationFields.DATABASE_LOGIN.fieldName].toString()
            )
            applySetting(
                "hibernate.connection.password",
                config[ConfigurationFields.DATABASE_PASSWORD.fieldName].toString()
            )
            applySetting(
                "hibernate.hbm2ddl.auto",
                config[ConfigurationFields.DATABASE_HBM.fieldName].toString()
            )
        }

        return serviceRegistry
    }
}