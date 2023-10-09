package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.persistance.beans.*
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.*
import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import org.junit.jupiter.api.Test

class HibernateIntegrationTest {

    @Test
    fun hibernateBuildTest(){

        val classes = listOf(
            PlayerBean::class.java, AreaPermission::class.java, ChunkBean::class.java,
            NationBean::class.java, NationPermission::class.java, NationPlot::class.java, PermissionGroup::class.java)
        LocalDatabase.loadAndBuildDB(classes)

    }
}