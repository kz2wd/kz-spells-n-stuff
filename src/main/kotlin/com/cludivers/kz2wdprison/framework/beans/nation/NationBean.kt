package com.cludivers.kz2wdprison.framework.beans.nation

import com.cludivers.kz2wdprison.framework.beans.PlayerBean
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
    var residents: List<PermissionGroup>? = null

    @ManyToMany
    var criminals: List<PlayerBean>? = null

    @OneToMany
    var chunks: List<ChunkBean>? = null

    @OneToMany
    var plots: List<NationPlot>? = null

    @OneToOne
    var defaultAreaRules: AreaPermission? = null

    var level: Int? = 0

    var chunkClaimTokens: Int? = 1

    fun description(): String{
        val residentAmount = if (residents.isNullOrEmpty()) 1 else residents?.map { if (!it.players.isNullOrEmpty()) it.players!!.size else 0}
            ?.reduce { acc, i -> acc + i }
        return "$name, $residentAmount citoyens, ${chunks?.size} chunks"
    }

    fun changeOwner(newOwner: PlayerBean){
        removeMember(newOwner)
        addMember(this.owner!!)
        this.owner = newOwner
    }

    fun addMember(member: PlayerBean){
        this.residents!![0].players?.add(member)
    }

    fun removeMember(member: PlayerBean){
        this.residents?.forEach { it.players?.remove(member) }
    }

    fun findNewOwner(): PlayerBean? {
        this.residents?.sortedBy { it.nationPermission?.rankAuthority }?.forEach {
            return it.players?.random()
        }
        return null
    }

    companion object {
        fun instantiateAndPersistDefaultNation(session: Session, owner: PlayerBean, name: String): NationBean {
            val nation = NationBean()
            nation.owner = owner
            nation.name = name

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
