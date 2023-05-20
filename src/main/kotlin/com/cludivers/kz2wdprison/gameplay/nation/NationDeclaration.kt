package com.cludivers.kz2wdprison.gameplay.nation

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.nation.*
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session

object NationDeclaration {

    fun declare(plugin: JavaPlugin, session: Session){

        val nationCommandName = "nation"


        val nationCommandsExecutor = MainCommandExecutor(
            mapOf(
                "create" to CreateNation(nationCommandName, session),
                "invite" to InviteToNation(nationCommandName, session),
                "join" to JoinNation(nationCommandName, session),
                "refuse" to RefuseInvitation(nationCommandName),
                "quit" to QuitNation(nationCommandName, session),
                "claim" to NationClaim(nationCommandName, session),
                "info" to NationInfo(nationCommandName, session),
                "add" to AddClaimPoint(nationCommandName, session)
            )
        )

        plugin.getCommand(nationCommandName)?.setExecutor(nationCommandsExecutor)
        plugin.getCommand(nationCommandName)?.tabCompleter = nationCommandsExecutor
    }
}