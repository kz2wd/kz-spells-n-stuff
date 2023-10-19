package com.cludivers.kz2wdprison.gameplay.artifact.effects

import com.cludivers.kz2wdprison.gameplay.artifact.inputs.ArtifactInput
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface ArtifactEffectInterface {
    fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?)
}