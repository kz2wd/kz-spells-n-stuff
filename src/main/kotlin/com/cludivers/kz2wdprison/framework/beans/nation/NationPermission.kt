package com.cludivers.kz2wdprison.framework.beans.nation

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.Session

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

    companion object {
        fun getPersistentCitizenPermissions(session: Session): NationPermission{
            val perm = NationPermission()
            session.persist(perm)
            return perm
        }

        fun getPersistentOfficerPermissions(session: Session): NationPermission{
            val nationPermission = NationPermission()
            nationPermission.canInvite = true
            session.persist(nationPermission)
            return nationPermission
        }
    }
}