package com.cludivers.kz2wdprison.framework.beans.nation

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne
import org.hibernate.Session

@Entity
class PermissionGroup {
    @Id
    @GeneratedValue
    var id: Long? = null

    var name: String? = null

    @OneToOne
    var areaPermission: AreaPermission? = null

    @OneToOne
    var nationPermission: NationPermission? = null

    @ManyToMany(mappedBy = "groups")
    var players: MutableList<PlayerBean>? = null

    companion object {

        fun getPersistentDefaultCitizenPermissionGroup(session: Session): PermissionGroup {
            val group = PermissionGroup()
            group.name = "Citoyens"
            group.areaPermission = AreaPermission.getPersistentCitizenPermissions(session)
            group.nationPermission = NationPermission.getPersistentCitizenPermissions(session)
            session.persist(group)
            return group
        }

        fun getPersistentDefaultOfficerPermissionGroup(session: Session): PermissionGroup {
            val group = PermissionGroup()
            group.name = "Officers"
            group.areaPermission = AreaPermission.getPersistentOfficerPermissions(session)
            group.nationPermission = NationPermission.getPersistentOfficerPermissions(session)
            session.persist(group)
            return group
        }

    }

}