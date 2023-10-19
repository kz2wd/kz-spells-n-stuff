package com.cludivers.kz2wdprison.gameplay.nation

import com.cludivers.kz2wdprison.gameplay.nation.beans.NationBean
import com.cludivers.kz2wdprison.gameplay.player.sendConfirmationMessage
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

    fun sendInvitationNotification(sender: Player, receiver: Player, nation: NationBean){
        receiver.sendConfirmationMessage("${sender.name} vous à invité à rejoindre la nation ${nation.name!!}",
            "/nation join ${sender.name}", "/nation refuse ${sender.name}")
    }

}