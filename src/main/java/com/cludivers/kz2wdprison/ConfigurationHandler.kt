package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.beans.PlayerBean
import org.bukkit.configuration.file.FileConfiguration
import org.hibernate.cfg.Configuration

class ConfigurationHandler {
    companion object {
        fun getConfiguration(config: FileConfiguration): Configuration {
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
            configuration.addAnnotatedClass(PlayerBean::class.java)
            return configuration
        }
    }
}