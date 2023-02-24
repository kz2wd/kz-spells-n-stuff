package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import com.cludivers.kz2wdprison.framework.beans.city.*
import org.junit.jupiter.api.Test

class HibernateIntegrationTest {

    @Test
    fun hibernateBuildTest(){

        val classes = listOf(PlayerBean::class.java, AreaPermission::class.java, ChunkBean::class.java,
            CityBean::class.java, CityPermission::class.java, CityPlot::class.java, PermissionGroup::class.java)
        LocalDatabase.loadAndBuildDB(classes)

    }
}