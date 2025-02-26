package com.cludivers.kz2wdprison.framework.commands

import com.cludivers.kz2wdprison.Kz2wdPrison
import io.github.classgraph.ClassGraph
import java.util.logging.Level
import java.util.logging.Logger

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ServerCommand(val command: String, val parent: MainCommandNames) {
    companion object {
        fun initializeCommands(packageName: String) {
            // Collect annotated subcommands
            val commands = ClassGraph()
                .acceptPackages(packageName)
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()
                .getClassesWithAnnotation(ServerCommand::class.java)
            val commandMap = mutableMapOf<String, MutableList<Pair<String, Class<out SubCommand>>>>()
            for (classInfo in commands) {
                val clazz = classInfo.loadClass() as? Class<out SubCommand> ?: continue
                val annotation = clazz.getAnnotation(ServerCommand::class.java) ?: continue
                val parentName = annotation.parent.commandName
                val commandName = annotation.command
                commandMap.computeIfAbsent(parentName) { mutableListOf() }.add(Pair(commandName, clazz))
            }
            // now that we have a map of commandName to command list, build the MainCommandExecutor
            commandMap.entries.forEach { (parentName, commands) ->
                val commandExecutor = MainCommandExecutor(commands.associate { it.first to it.second.getDeclaredConstructor().newInstance() })
                Kz2wdPrison.plugin.getCommand(parentName)?.setExecutor(commandExecutor)
                Kz2wdPrison.plugin.getCommand(parentName)?.tabCompleter = commandExecutor
                Logger.getGlobal().log(Level.INFO, "Init cmd $parentName with cmds ${commands.joinToString(", ") { it.first }}")
            }
        }
    }
}
