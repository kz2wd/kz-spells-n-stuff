package com.cludivers.kz2wdprison.modules.attributes

import com.cludivers.kz2wdprison.framework.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.modules.attributes.commands.*
import org.bukkit.plugin.java.JavaPlugin

object AttributesDeclaration {
    fun declare(plugin: JavaPlugin) {

        val attributeItemCommandName = "intrinsic"
        val attributeItemCommandExecutor = MainCommandExecutor(
            mapOf(
                "create" to AttributeItemAdd(attributeItemCommandName),
                "help" to AttributeHelpCommand(attributeItemCommandName),
                "fill" to AttributeItemFill(attributeItemCommandName),
                "unfill" to AttributeItemUnfill(attributeItemCommandName),
                "info" to AttributeItemInfo(attributeItemCommandName),
            )
        )

        plugin.getCommand(attributeItemCommandName)?.setExecutor(attributeItemCommandExecutor)
        plugin.getCommand(attributeItemCommandName)?.tabCompleter = attributeItemCommandExecutor
    }
}