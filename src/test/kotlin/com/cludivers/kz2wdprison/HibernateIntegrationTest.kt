package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.configuration.HibernateConfigurationHandler
import org.junit.jupiter.api.Test

class HibernateIntegrationTest {

    @Test
    fun hibernateBuildTest(){

        HibernateConfigurationHandler.loadSessionFactory(null)

    }
}