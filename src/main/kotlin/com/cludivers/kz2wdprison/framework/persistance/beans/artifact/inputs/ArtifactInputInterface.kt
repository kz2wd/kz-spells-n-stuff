package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Caster
import org.bukkit.inventory.ItemStack

interface ArtifactInputInterface {

    fun enrichArtifactInput(
        inputRune: ItemStack,
        caster: Caster,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>
    )
}