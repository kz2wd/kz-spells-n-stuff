package com.cludivers.kz2wdprison.artifact

import com.cludivers.kz2wdprison.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.artifact.beans.Artifact
import com.cludivers.kz2wdprison.artifact.runes.RunesBehaviors
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.time.Duration


class ArtifactExecution(
    // Constant fields
    var activator: ArtifactActivator,
    var player: Player?,

    // Field changing at every step
    var inputRune: ItemStack,

    // Artifact logic fields
    var locations: MutableList<Location> = mutableListOf(),
    var entities: MutableList<Entity> = mutableListOf(),
    var directions: MutableList<Vector> = mutableListOf(),

    var flow: Float = 0f,
    var duration: Duration = Duration.ZERO,
    var enableRequirements: Boolean = true,

    // Execution related fields
    var inputsTrace: MutableList<ItemStack> = mutableListOf(),
    private val runesOrdered: List<ItemStack>,
    private var runeIndex: Int = 0,
    private val artifact: Artifact,
) {
    fun nextActivation() {
        if (runesOrdered.size <= runeIndex) {
            PluginConfiguration.session.beginTransaction()
            artifact.cooldown = duration.plus(artifact.cooldownDebuff)
            PluginConfiguration.session.transaction.commit()
            return
        }

        inputRune = runesOrdered[runeIndex++]
        RunesBehaviors.processArtifactActivation(this)
    }

}


