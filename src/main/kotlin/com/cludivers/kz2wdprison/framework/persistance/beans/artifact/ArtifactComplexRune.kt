package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.ArtifactEffectInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInputInterface
import com.cludivers.kz2wdprison.framework.persistance.converters.ItemStackConverter
import jakarta.persistence.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hibernate.Session

@Entity
class ArtifactComplexRune : ArtifactInputInterface, ArtifactEffectInterface {

    companion object {
        val artifactComplexRunes: MutableMap<ItemStack, ArtifactComplexRune> = mutableMapOf()

        fun registerArtifactComplexRune(rune: ArtifactComplexRune, itemStack: ItemStack) {
            artifactComplexRunes[itemStack] = rune
        }

        fun isItemStackLinked(item: ItemStack): Boolean {
            return artifactComplexRunes.containsKey(item)
        }

        fun initPersistentArtifactComplexRune(session: Session) {
            val artifactComplexInputs =
                session.createQuery("from ArtifactComplexRune A", ArtifactComplexRune::class.java).list()
            artifactComplexInputs.filter { it.linkedItemStack != null }
                .forEach { registerArtifactComplexRune(it, it.linkedItemStack!!) }
        }

    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    @ElementCollection(targetClass = ItemStack::class)
    @Convert(attributeName = "value", converter = ItemStackConverter::class)
    var stockedItemStack: Map<Int, ItemStack> = mapOf()

    var runeType: ArtifactRuneTypes = ArtifactRuneTypes.NONE


    override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
        // Do nothing
    }

    override fun enrichArtifactInput(
        inputRune: ItemStack,
        caster: Caster,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>
    ) {
        // Prevent infinite loop

        if (inputsTrace.contains(inputRune)) {
            return
        }
        inputsTrace.add(inputRune)

        stockedItemStack.forEach {
            ArtifactRuneTypes.GENERIC_INPUT_RUNE.enrichArtifactInput(
                it.value,
                caster,
                input,
                inputsTrace
            )
        }
    }
}

