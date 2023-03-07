package com.cludivers.kz2wdprison.framework.beans.nation

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.Session

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
        fun getPersistentCitizenPermissions(session: Session): AreaPermission{
            val perm = AreaPermission()
            session.persist(perm)
            return perm
        }

        fun getPersistentOfficerPermissions(session: Session): AreaPermission{
            val areaPermission = AreaPermission()
            areaPermission.canBreak = true
            areaPermission.canPlace = true
            areaPermission.canAttackPlayer = true
            areaPermission.canAttackFriendlyMob = true
            session.persist(areaPermission)
            return areaPermission
        }

    }
}