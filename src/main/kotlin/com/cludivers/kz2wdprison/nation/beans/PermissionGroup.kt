package com.cludivers.kz2wdprison.nation.beans

import com.cludivers.kz2wdprison.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.persistence.beans.player.PlayerBean
import jakarta.persistence.*
import org.bukkit.Bukkit
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

        fun getPersistentDefaultCitizenPermissionGroup(): PermissionGroup {
            val group = PermissionGroup()
            group.name = "Citoyens"
            group.areaPermission = AreaPermission.getPersistentCitizenPermissions()
            group.nationPermission = NationPermission.getPersistentCitizenPermissions()
            PluginConfiguration.session.persist(group)
            return group
        }

        fun getPersistentDefaultOfficerPermissionGroup(): PermissionGroup {
            val group = PermissionGroup()
            group.name = "Officers"
            group.areaPermission = AreaPermission.getPersistentOfficerPermissions()
            group.nationPermission = NationPermission.getPersistentOfficerPermissions()
            PluginConfiguration.session.persist(group)
            return group
        }

    }

}