package com.cludivers.kz2wdprison.nation

import com.cludivers.kz2wdprison.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.nation.commands.*
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object NationDeclaration {

    const val NATION_COMMAND_NAME = "nation"

    fun declare(plugin: JavaPlugin, server: Server) {

        val nationCommandsExecutor = MainCommandExecutor(
            mapOf(
                "create" to CreateNation(),
                "invite" to InviteToNation(),
                "join" to JoinNation(),
                "refuse" to RefuseInvitation(),
                "quit" to QuitNation(),
                "claim" to NationClaim(),
                "info" to NationInfo(),
                "get_claim_point" to AddClaimPoint(),
                "fill" to AddShardsToNation(),
                "attack" to AttackNation(),
                "show_attacks" to ShowPendingAttacks(),
            )
        )

        plugin.getCommand(NATION_COMMAND_NAME)?.setExecutor(nationCommandsExecutor)
        plugin.getCommand(NATION_COMMAND_NAME)?.tabCompleter = nationCommandsExecutor

//        server.pluginManager.registerEvents(NationListener(), plugin)
    }
}