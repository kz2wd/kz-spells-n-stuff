package com.cludivers.kz2wdprison.framework.configuration

import org.bukkit.configuration.file.FileConfiguration
import org.hibernate.Session
import org.hibernate.SessionFactory


object PluginConfiguration {
    lateinit var session: Session
    private lateinit var sessionFactory: SessionFactory
    var isDatabaseConnected: Boolean = false
    var isInProduction: Boolean = false

    fun loadConfigurationAndDatabase(file: FileConfiguration) {
        isInProduction = file[ConfigurationFields.IS_IN_PRODUCTION.fieldName].toString().toBoolean()
        isDatabaseConnected = file[ConfigurationFields.CONNECT_DATABASE.fieldName].toString().toBoolean()
        sessionFactory = HibernateConfigurationHandler.loadSessionFactory(file)
        session = sessionFactory.openSession()
    }

    fun closeConnections() {
        session.close()
        sessionFactory.close()
    }

}