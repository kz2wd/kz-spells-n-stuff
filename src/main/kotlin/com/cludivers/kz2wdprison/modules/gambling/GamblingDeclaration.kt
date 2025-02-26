package com.cludivers.kz2wdprison.modules.gambling

import com.cludivers.kz2wdprison.framework.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.modules.gambling.commands.GambleCommand
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

object GamblingDeclaration {

    fun declare(plugin: JavaPlugin, server: Server) {

        val gambleCommandName = "gamble"
        val gambleCommandExecutor = MainCommandExecutor(
            mapOf(
                "classic" to GambleCommand(gambleCommandName),
            )
        )

        plugin.getCommand(gambleCommandName)?.setExecutor(gambleCommandExecutor)
        plugin.getCommand(gambleCommandName)?.tabCompleter = gambleCommandExecutor
    }
}