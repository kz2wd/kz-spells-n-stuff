package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface ArtifactEffectInterface {
    fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?)
}