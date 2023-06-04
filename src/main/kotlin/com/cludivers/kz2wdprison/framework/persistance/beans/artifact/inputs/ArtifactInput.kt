package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.util.Vector


class ArtifactInput(
    var flow: Int = 0,
    var locations: List<Location> = listOf(),
    var entities: List<Entity> = listOf(),
    var vectors: List<Vector> = listOf(),
    var maxDistance: Int = 5
)


