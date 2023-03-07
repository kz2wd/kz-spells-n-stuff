package com.cludivers.kz2wdprison.gameplay.nation

import com.cludivers.kz2wdprison.framework.beans.nation.NationBean
import org.bukkit.entity.Player

object NationInvitation {

    private val allInvitations: MutableMap<Pair<Player, Player>, NationBean> = mutableMapOf()

    fun addInvitation(sender: Player, receiver: Player, nation: NationBean){
        allInvitations[Pair(sender, receiver)] = nation
    }

    fun doInvitationExist(sender: Player, receiver: Player): Boolean{
        return allInvitations.containsKey(Pair(sender, receiver))
    }

    fun removeInvitation(sender: Player, receiver: Player): NationBean?{
        val key = Pair(sender, receiver)
        val nation = allInvitations[key]
        allInvitations.remove(key)
        return nation
    }
}