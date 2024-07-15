package com.cludivers.kz2wdprison.configuration.database

import org.hibernate.boot.registry.StandardServiceRegistryBuilder

object LocalDatabase {
    fun loadLocalDatabase(): StandardServiceRegistryBuilder {

        val serviceRegistry = StandardServiceRegistryBuilder().apply {

            //DataSource Config
            applySetting("hibernate.connection.driver_class", "org.h2.Driver")
            applySetting("dialect", "org.hibernate.dialect.H2Dialect")
            applySetting("hibernate.connection.url", "jdbc:h2:mem:test")
            applySetting("hibernate.connection.username", "sa")
            applySetting("hibernate.connection.password", "")
            applySetting("hibernate.connection.characterEncoding", "utf8")
            applySetting("show_sql", true)
            applySetting("format_sql", true)
            applySetting("use_sql_comments", true)
            applySetting("hibernate.hbm2ddl.auto", "create-drop")
            applySetting("hibernate.current_session_context_class", "thread")
        }

        return serviceRegistry
    }
}