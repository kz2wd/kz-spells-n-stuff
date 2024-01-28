package com.cludivers.kz2wdprison.gameplay.worldgeneration.worldgenerator

import net.kyori.adventure.util.TriState
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin

object WorldGenerator {
    var worlds: MutableList<WorldRules> = mutableListOf()

    lateinit var plugin: JavaPlugin
    fun generateNewWorld() {

//        PluginConfiguration.session.beginTransaction()
//
//        val customWorld = WorldRules()
//
//        PluginConfiguration.session.persist(customWorld)
//
//        PluginConfiguration.session.transaction.commit()
//
//        worlds.add(customWorld)

        Bukkit.getWorlds()

        class CustomChunkGenerator : ChunkGenerator() {

        }

        val customChunkGenerator = CustomChunkGenerator()

        class CustomWorldCreator : WorldCreator("testWorld1") {

            override fun generator(): ChunkGenerator {
                return customChunkGenerator
            }

            override fun keepSpawnLoaded(): TriState {
                return TriState.FALSE
            }
        }

        Bukkit.createWorld(CustomWorldCreator())


    }
}