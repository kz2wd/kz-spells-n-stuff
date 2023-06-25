package com.cludivers.kz2wdprison.gameplay.nation

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.nation.*
import org.bukkit.plugin.java.JavaPlugin

object NationDeclaration {

    fun declare(plugin: JavaPlugin) {

        val nationCommandName = "nation"


        val nationCommandsExecutor = MainCommandExecutor(
            mapOf(
                "create" to CreateNation(nationCommandName),
                "invite" to InviteToNation(nationCommandName),
                "join" to JoinNation(nationCommandName),
                "refuse" to RefuseInvitation(nationCommandName),
                "quit" to QuitNation(nationCommandName),
                "claim" to NationClaim(nationCommandName),
                "info" to NationInfo(nationCommandName),
                "add" to AddClaimPoint(nationCommandName)
            )
        )

        plugin.getCommand(nationCommandName)?.setExecutor(nationCommandsExecutor)
        plugin.getCommand(nationCommandName)?.tabCompleter = nationCommandsExecutor
    }
}