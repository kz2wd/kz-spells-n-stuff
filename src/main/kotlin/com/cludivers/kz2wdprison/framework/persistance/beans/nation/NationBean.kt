package com.cludivers.kz2wdprison.framework.persistance.beans.nation

import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.hibernate.Session
import org.hibernate.annotations.CreationTimestamp
import java.sql.Date

@Entity
class NationBean {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var name: String? = null

    // Owner
    @OneToOne
    var owner: PlayerBean? = null

    @CreationTimestamp
    var creationDate: Date? = null

    @OneToMany
    var residents: MutableList<PermissionGroup>? = null

    @ManyToMany
    var criminals: MutableList<PlayerBean>? = null

    @OneToMany
    var chunks: MutableList<ChunkBean>? = null

    @OneToMany
    var plots: MutableList<NationPlot>? = null

    @OneToOne
    var defaultAreaRules: AreaPermission? = null

    var level: Int? = 0

    var chunkClaimTokens: Int? = 1

    fun description(): String{
        val residentAmount: Int = if (residents.isNullOrEmpty()) 0 else residents!!.map {
            if (!it.players.isNullOrEmpty()) it.players!!.size else 0
        }.reduce { acc, i -> acc + i }
        return "$name, ${residentAmount + 1} citoyens, ${chunks?.size} chunks\n${residents!!.map { it.description() }.joinToString("\n")}"
    }

    fun changeOwner(newOwner: PlayerBean){
        removeMember(newOwner)
        addMember(this.owner!!)
        this.owner = newOwner
    }

    fun addMember(member: PlayerBean){
        member.nation = this
        member.permissionGroup = this.residents!![0]
        this.residents!![0].players?.add(member)
    }

    fun removeMember(member: PlayerBean){
        this.residents?.forEach { it.players?.remove(member) }
        member.nation = null
    }

    fun findNewOwner(): PlayerBean? {
        this.residents?.sortedBy { it.nationPermission?.rankAuthority }?.forEach {
            if (!it.players.isNullOrEmpty()){
                return it.players?.random()
            }
        }
        return null
    }

    fun delete(session: Session){
        this.owner!!.nation = null
        this.residents!!.forEach { group ->
            group.players!!.forEach {
                it.nation = null
                it.permissionGroup = null
            }
            val areaPerm = group.areaPermission
            group.areaPermission = null
            session.remove(areaPerm)
            val nationPerm = group.nationPermission
            group.nationPermission = null
            session.remove(nationPerm)
        }
        this.chunks!!.forEach {
            it.nation = null
        }
    }

    companion object {
        fun instantiateAndPersistDefaultNation(session: Session, owner: PlayerBean, name: String): NationBean {
            val nation = NationBean()
            nation.owner = owner
            nation.name = name

            nation.defaultAreaRules = AreaPermission.getPersistentDefaultPermissions(session)

            nation.residents = mutableListOf(
                PermissionGroup.getPersistentDefaultCitizenPermissionGroup(session),
                PermissionGroup.getPersistentDefaultOfficerPermissionGroup(session)
            )
            nation.chunks = mutableListOf()
            nation.level = 1
            nation.plots = mutableListOf()
            session.persist(nation)

            return nation
        }
    }

}
