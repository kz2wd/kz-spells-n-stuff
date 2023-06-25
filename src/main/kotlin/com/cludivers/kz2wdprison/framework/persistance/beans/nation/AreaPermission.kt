package com.cludivers.kz2wdprison.framework.persistance.beans.nation

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
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
            HibernateSession.session.persist(perm)
            return perm
        }

        fun getPersistentCitizenPermissions(): AreaPermission {
            val perm = AreaPermission()
            HibernateSession.session.persist(perm)
            return perm
        }

        fun getPersistentOfficerPermissions(): AreaPermission {
            val areaPermission = AreaPermission()
            areaPermission.canBreak = true
            areaPermission.canPlace = true
            areaPermission.canAttackPlayer = true
            areaPermission.canAttackFriendlyMob = true
            HibernateSession.session.persist(areaPermission)
            return areaPermission
        }

    }
}