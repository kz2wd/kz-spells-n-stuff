package com.cludivers.kz2wdprison.framework.persistance.beans.nation

import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import jakarta.persistence.*
import org.bukkit.Bukkit
import org.hibernate.Session
import java.util.*

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

    @OneToMany
    var players: MutableList<PlayerBean>? = mutableListOf()

    fun description(): String{
        return "- $name : ${players!!.map { Bukkit.getOfflinePlayer(UUID.fromString(it.uuid!!)).name }.joinToString(", ")}"
    }

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