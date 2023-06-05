package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.converters

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import org.bukkit.inventory.ItemStack

interface ArtifactConverterInterface {
    fun convertInput(itemStack: ItemStack, input: ArtifactInput)
}