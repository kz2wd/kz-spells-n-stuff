package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface ArtifactRuneInterface {

    // The player is only needed for block placing or destroying event, to get permissions
    fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?,
            nextActivation: ((ArtifactInput) -> ArtifactInput) -> Unit
    )

    fun triggerNext(nextActivation: ((ArtifactInput) -> ArtifactInput) -> Unit)

}