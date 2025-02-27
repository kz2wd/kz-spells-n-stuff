package com.cludivers.kz2wdprison.modules.nation.beans

import com.cludivers.kz2wdprison.framework.configuration.FetchAfterDatabaseInit
import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.nation.NationInvitation
import com.cludivers.kz2wdprison.modules.player.PlayerBean
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.modules.player.notifyPlayer
import com.cludivers.kz2wdprison.modules.player.sendErrorMessage
import com.cludivers.kz2wdprison.modules.player.sendSuccessMessage
import com.cludivers.kz2wdprison.modules.shardsworld.PlotState
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.hibernate.annotations.CreationTimestamp
import java.sql.Date

@Entity
class NationBean {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var name: String = ""

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
    var plots: MutableList<PlotState>? = null


    @OneToOne
    var defaultAreaRules: AreaPermission? = null

    var level: Int = 0

    var shards: Int = 0

    // The idea is that players put shards into their nation, and as long as a nation is charged with shards, it can enforce
    // their rules.

    // Other players can attack the nation by using shards. The protected ratio increase the amount needed for attackers
    // if protected tier is at 3, a nation containing 100 shards will require 300 shards to be destroyed.

    // The greater the protectionRatio, the shorter the attacks influence
    // Attacking a nation with shards doesn't instantly make it lose shards. Let's say, by default, it is 24h

    // Attackers can attack with the amount they want.
    @Enumerated
    var protectionTier: ProtectionTiers = ProtectionTiers.TIER2

    @ManyToMany
    var pendingAttacks: MutableList<NationAttack>? = null

    var chunkClaimTokens: Int = 5

    fun description(): String{
        val residentAmount: Int = if (residents.isNullOrEmpty()) 0 else residents!!.map {
            if (!it.players.isNullOrEmpty()) it.players!!.size else 0
        }.reduce { acc, i -> acc + i }
        return "$name, ${residentAmount + 1} citoyens, ${plots?.size} plots\n${residents!!.map { it.description() }.joinToString("\n")}"
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

    fun delete() {
        removeNationName(this)
        this.owner!!.nation = null
        this.residents!!.forEach { group ->
            group.players!!.forEach {
                it.nation = null
                it.permissionGroup = null
            }
            val areaPerm = group.areaPermission
            group.areaPermission = null
            PluginConfiguration.session.remove(areaPerm)
            val nationPerm = group.nationPermission
            group.nationPermission = null
            PluginConfiguration.session.remove(nationPerm)
        }
        this.plots!!.forEach {
            it.plotX = null
        }
    }

    // If player resolution from uuid is bad, this function will suck, performance wise
    fun notifyPlayers(content: Component, sound: Sound? = null) {
        residents?.forEach { group -> group.players?.forEach { Bukkit.getPlayer(it.uuid!!)?.notifyPlayer(content, sound, 1f, true) } }
    }

    companion object {

        @FetchAfterDatabaseInit
        private fun fetchNations() {
            val query = PluginConfiguration.session.createQuery("from NationBean", NationBean::class.java)
            nationsFromName = query.list().associateBy { it.name }.toMutableMap()
        }

        private lateinit var nationsFromName: MutableMap<String, NationBean>
        fun getNationFromName(name: String): NationBean? {
            return nationsFromName[name]
        }

        fun getNationMatching(name: String): Map<String, NationBean> {
            return nationsFromName.filter { name in it.key }
        }

        private fun addNationName(nationBean: NationBean) {
            nationsFromName[nationBean.name] = nationBean
        }

        private fun removeNationName(nationBean: NationBean) {
            nationsFromName.remove(nationBean.name)
        }

        private fun instantiateAndPersistDefaultNation(owner: PlayerBean, name: String): NationBean {
            val nation = NationBean()
            nation.owner = owner
            nation.name = name

            nation.defaultAreaRules = AreaPermission.getPersistentDefaultPermissions()

            nation.residents = mutableListOf(
                PermissionGroup.getPersistentDefaultCitizenPermissionGroup(),
                PermissionGroup.getPersistentDefaultOfficerPermissionGroup()
            )
            nation.level = 1
            nation.plots = mutableListOf()
            PluginConfiguration.session.persist(nation)

            return nation
        }

        fun Player.createNation() {
            val playerData = this.getData()
            if (playerData.nation is NationBean) {
                this.sendErrorMessage("Vous appartenez déjà à une nation")
                return
            }
            PluginConfiguration.session.beginTransaction()

            val nation = instantiateAndPersistDefaultNation(playerData, "Nation de ${this.name}")

            playerData.nation = nation

            PluginConfiguration.session.transaction.commit()
            this.sendSuccessMessage("Votre nation vient d'être créée !")

            addNationName(nation)
        }

        fun Player.invitePlayerToNation(invitedPlayer: Player) {

            val senderData = this.getData()
            if (senderData.nation !is NationBean) {
                this.sendErrorMessage("Vous n'appartenez à aucune nation")
                return
            }
            if (invitedPlayer == this) {
                this.sendErrorMessage("Vous essayez de vous inviter vous-même ?")
                return
            }
            if (senderData.nation == invitedPlayer.getData().nation) {
                this.sendErrorMessage("${invitedPlayer.name} est déjà dans votre nation")
                return
            }

            if (NationInvitation.doInvitationExist(this, invitedPlayer)) {
                this.sendErrorMessage("Vous avez déjà invité ${invitedPlayer.name}");
                return
            }
            NationInvitation.addInvitation(this, invitedPlayer, senderData.nation!!)
            NationInvitation.sendInvitationNotification(this, invitedPlayer, senderData.nation!!)
            this.sendSuccessMessage("Invitation envoyée à ${invitedPlayer.name}");
        }

        fun Player.acceptNationInvitation(playerInviting: Player) {

            val playerData = this.getData()

            if (playerData.nation is NationBean) {
                this.sendErrorMessage("Vous appartenez déjà à une nation, quittez la pour en rejoindre une autre")
                return
            }

            val nation = NationInvitation.removeInvitation(playerInviting, this)
            if (nation !is NationBean) {
                this.sendErrorMessage("Vous n'avez pas été invité par cette personne")
                return
            }

            PluginConfiguration.session.beginTransaction()
            nation.addMember(playerData)
            PluginConfiguration.session.transaction.commit()
            this.sendSuccessMessage("Vous avez rejoint la nation ${nation.name}")
        }

        fun Player.refuseNationInvitation(playerInviting: Player) {

            val nation = NationInvitation.removeInvitation(playerInviting, this)
            if (nation !is NationBean) {
                this.sendErrorMessage("Cette personne ne vous à pas invitée")
                return
            }
            this.sendSuccessMessage("Vous avez refusé l'invitation à rejoindre la nation : ${nation.name}")
        }

        fun Player.quitNation() {
            val playerData = this.getData()
            val nation = playerData.nation
            if (nation !is NationBean) {
                this.sendErrorMessage("Vous n'appartenez à aucune nation")
                return
            }
            PluginConfiguration.session.beginTransaction()
            nation.removeMember(playerData)

            if (nation.owner == playerData) {
                val newOwner = nation.findNewOwner()
                if (newOwner !is PlayerBean) {
                    nation.delete()
                    this.sendSuccessMessage("Votre nation à été supprimée")
                } else {
                    nation.changeOwner(newOwner)

                }
            }
            PluginConfiguration.session.transaction.commit()
            this.sendSuccessMessage("Vous avez quitté votre nation")
        }

        fun Player.claimPlot() {
            val playerData = this.getData()
            val playerNation = playerData.nation
            if (playerNation !is NationBean) {
                this.sendErrorMessage("Vous n'appartenez à aucune nation")
                return
            }

            if (!(playerData == playerNation.owner || playerData.permissionGroup!!.nationPermission!!.canClaim)) {
                this.sendErrorMessage("Vous n'avez pas la permission pour vous approprier des chunks")
                return
            }

            val plotState = PlotState.getPlotState(this.location)
            if (plotState == null) {
                this.sendErrorMessage("Ce plot n'existe pas...")
                return
            }
            if (plotState.owningNation is NationBean) {
                if (plotState.owningNation == playerNation) {
                    this.sendErrorMessage("Your nation already own this island.")
                } else {
                    this.sendErrorMessage("This island is not available")
                }
                return
            }

            if (playerNation.chunkClaimTokens <= 0) {
                this.sendErrorMessage("Vous n'avez plus de jeton d'appropriation de chunk")
                return
            }

            PluginConfiguration.session.beginTransaction()

            playerNation.chunkClaimTokens -= 1
            playerNation.plots!!.add(plotState)
            plotState.owningNation = playerNation
            PluginConfiguration.session.transaction.commit()

            this.sendSuccessMessage("Le chunk à bien été approprié")
        }

        fun Player.showPendingAttacks() {
            val playerData = this.getData()
            val playerNation = playerData.nation
            if (playerNation !is NationBean) {
                this.sendErrorMessage("Vous n'appartenez à aucune nation")
                return
            }

            playerNation.pendingAttacks!!.forEach { this.sendMessage(it.compactDescription()) }
            if (playerNation.pendingAttacks!!.isEmpty()) {
                this.sendMessage(Component.text("Il n'y aucune attaque sur votre nation en ce moment"))
            }

        }
    }

}
