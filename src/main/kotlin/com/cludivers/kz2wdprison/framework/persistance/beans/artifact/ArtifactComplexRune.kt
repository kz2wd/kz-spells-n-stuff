package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects.ArtifactEffectInterface
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInputInterface
import com.cludivers.kz2wdprison.framework.persistance.converters.ItemStackConverter
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespaces
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespacesManager
import jakarta.persistence.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Entity
class ArtifactComplexRune : ArtifactInputInterface, ArtifactEffectInterface {

    companion object {
        fun getComplexRune(itemStack: ItemStack): ArtifactComplexRune? {
            val uuid =
                CustomNamespacesManager.int[CustomNamespaces.COMPLEX_RUNE_UUID]!!.getData(itemStack.itemMeta)
                    ?: return null
            return HibernateSession.session
                .createQuery("from ArtifactComplexRune A where A.id = :uuid", ArtifactComplexRune::class.java)
                .setParameter("uuid", uuid.toString())
                .uniqueResult()
        }

        fun createComplexRune(item: ItemStack, type: ArtifactRuneTypes): ArtifactComplexRune {
            HibernateSession.session.beginTransaction()
            val complexRune = ArtifactComplexRune()
            complexRune.linkedItemStack = item
            complexRune.runeType = type
            HibernateSession.session.persist(complexRune)
            val meta = item.itemMeta
            CustomNamespacesManager.int[CustomNamespaces.COMPLEX_RUNE_UUID]!!.setData(meta, complexRune.id!!.toInt())
            item.itemMeta = meta
            HibernateSession.session.transaction.commit()
            return complexRune
        }
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(columnDefinition = "varbinary(500)")
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
        artifactActivator: ArtifactActivator,
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
                artifactActivator,
                input,
                inputsTrace
            )
        }
    }
}

