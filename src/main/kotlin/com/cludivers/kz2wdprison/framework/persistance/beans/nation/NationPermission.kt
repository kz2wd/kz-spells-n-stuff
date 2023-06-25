package com.cludivers.kz2wdprison.framework.persistance.beans.nation

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class NationPermission {
    @Id
    @GeneratedValue
    var id: Long? = null

    var rankAuthority: Int = 0

    var canInvite: Boolean = false
    var canBan: Boolean = false
    var canManagePermission = false
    var canSetCriminal: Boolean = false
    var canClaim: Boolean = false

    companion object {
        fun getPersistentCitizenPermissions(): NationPermission {
            val perm = NationPermission()
            HibernateSession.session.persist(perm)
            return perm
        }

        fun getPersistentOfficerPermissions(): NationPermission {
            val nationPermission = NationPermission()
            nationPermission.canInvite = true
            HibernateSession.session.persist(nationPermission)
            return nationPermission
        }
    }
}