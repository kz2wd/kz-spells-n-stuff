package com.cludivers.kz2wdprison.gameplay.shardsworld

import net.kyori.adventure.util.TriState
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getLogger
import org.bukkit.Material
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Level

object WorldGenerator {

    lateinit var plugin: JavaPlugin
    fun generateNewWorld(worldName: String) {
        if (worldName.isBlank()) {
            throw Exception("Blank name cannot be accepted")
        }
        if (worldName in Bukkit.getWorlds().map { it.name }) {
            throw Exception("World $worldName already exist")
        }

        class CustomChunkGenerator : ChunkGenerator() {
            override fun generateSurface(
                worldInfo: WorldInfo,
                random: Random,
                chunkX: Int,
                chunkZ: Int,
                chunkData: ChunkData
            ) {
//                val radius = 5
//                if ((chunkZ * chunkZ) + (chunkX + chunkX) > radius * radius) {
//                    return
//                }
                chunkData.setBlock(0, 0, 50, Material.DIRT)
                chunkData.setBlock(1, 1, 50, Material.DIRT)
                for (i in 0 until 16) {
                    for (j in 0 until 16) {

                        chunkData.setBlock(i, j, 50, Material.DIRT)
                        chunkData.setBlock(i, j, 51, Material.DIRT)
                        chunkData.setBlock(i, j, 52, Material.DIRT)
                        chunkData.setBlock(i, j, 53, Material.GRASS_BLOCK)

                    }
                }
            }
        }


        class CustomWorldCreator : WorldCreator(worldName) {

            override fun generator(): ChunkGenerator {
                getLogger().log(Level.INFO, "CustomChunkGenerator is used!");
                return CustomChunkGenerator()
            }

            override fun keepSpawnLoaded(): TriState {
                return TriState.FALSE
            }
        }

        val world = Bukkit.createWorld(CustomWorldCreator()) ?: throw Exception("Could not create world")
        WorldState.registerNewWorld(world, 1.0f)

    }
}