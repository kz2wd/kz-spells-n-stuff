package com.cludivers.kz2wdprison.modules.nation.beans

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class AreaPermission {
    @Id
    @GeneratedValue
    var id: Long? = null

    var canBreak: Boolean = false
    var canPlace: Boolean = false
    var canAttackPlayer: Boolean = false
    var canAttackFriendlyMob = false
    var canAttackAggressiveMob = true
    
    companion object {

        fun getPersistentDefaultPermissions(): AreaPermission {
            val perm = AreaPermission()
            PluginConfiguration.session.persist(perm)
            return perm
        }

        fun getPersistentCitizenPermissions(): AreaPermission {
            val perm = AreaPermission()
            PluginConfiguration.session.persist(perm)
            return perm
        }

        fun getPersistentOfficerPermissions(): AreaPermission {
            val areaPermission = AreaPermission()
            areaPermission.canBreak = true
            areaPermission.canPlace = true
            areaPermission.canAttackPlayer = true
            areaPermission.canAttackFriendlyMob = true
            PluginConfiguration.session.persist(areaPermission)
            return areaPermission
        }

    }
}