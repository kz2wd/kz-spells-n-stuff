package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.ArtifactEffectInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.BasicArtifactEffects
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInputInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.BasicInputRunes
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class ArtifactRuneTypes : ArtifactInputInterface, ArtifactEffectInterface {
    INPUT_RUNE {
        override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
            val basicRune = BasicInputRunes.getInputRune(inputRune)
            if (basicRune != null) {
                return basicRune.getArtifactInput(inputRune, caster, inFlow)
            }

            // If it is not a basic input type :
            val complexRune = ArtifactComplexRune.artifactComplexRunes[inputRune]
            if (complexRune == null || complexRune.runeType != INPUT_RUNE) {
                return super.getArtifactInput(inputRune, caster, inFlow)
            }
            // If a complexRune of type INPUT was found, then resolve its input
            return complexRune.getArtifactInput(inputRune, caster, inFlow)
        }
    },
    EFFECT_RUNE {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            BasicArtifactEffects.getEffectType(itemStack).triggerArtifactEffect(itemStack, input, player)
        }
    },
    NONE;

    override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
        // Do nothing
    }

    override fun getArtifactInput(inputRune: ItemStack, caster: Caster, inFlow: Int): ArtifactInput {
        return ArtifactInput(0)
    }
}