package com.cludivers.kz2wdprison.gameplay.artifact.inputs

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector


class ArtifactInput(
    var flow: Float = 0f,
    var locations: List<Location> = listOf(),
    var entities: List<Entity> = listOf(),
    var vectors: List<Vector> = listOf(),
)


