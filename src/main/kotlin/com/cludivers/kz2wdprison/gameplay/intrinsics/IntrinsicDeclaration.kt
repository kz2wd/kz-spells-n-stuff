package com.cludivers.kz2wdprison.gameplay.intrinsics

import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.intrinsic.*
import org.bukkit.plugin.java.JavaPlugin

object IntrinsicDeclaration {
    fun declare(plugin: JavaPlugin) {

        val attributeItemCommandName = "intrinsic"
        val attributeItemCommandExecutor = MainCommandExecutor(
            mapOf(
                "create" to AttributeItemAdd(attributeItemCommandName),
                "help" to IntrinsicHelpCommand(attributeItemCommandName),
                "fill" to AttributeItemFill(attributeItemCommandName),
                "unfill" to AttributeItemUnfill(attributeItemCommandName),
                "info" to AttributeItemInfo(attributeItemCommandName),
            )
        )

        plugin.getCommand(attributeItemCommandName)?.setExecutor(attributeItemCommandExecutor)
        plugin.getCommand(attributeItemCommandName)?.tabCompleter = attributeItemCommandExecutor
    }
}