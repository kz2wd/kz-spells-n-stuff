package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactActivator
import org.bukkit.inventory.ItemStack

interface ArtifactInputInterface {

    fun enrichArtifactInput(
        inputRune: ItemStack,
        artifactActivator: ArtifactActivator,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>
    )
}