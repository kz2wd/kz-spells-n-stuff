package com.cludivers.kz2wdprison.framework.persistance.beans.artifact

import com.cludivers.kz2wdprison.framework.configuration.HibernateSession
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.framework.persistance.converters.ItemStackConverter
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespaces
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespacesManager
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.time.Instant
import kotlin.jvm.Transient

@Entity
class Artifact {

    companion object {
        val defaultItemStack = ItemStack(Material.AIR)

        fun getArtifact(itemStack: ItemStack): Artifact? {
            val uuid =
                CustomNamespacesManager.int[CustomNamespaces.ARTIFACT_UUID]!!.getData(itemStack.itemMeta) ?: return null
            return HibernateSession.session
                .createQuery("from Artifact A where A.id = :uuid", Artifact::class.java)
                .setParameter("uuid", uuid.toString())
                .uniqueResult()
        }

        fun createArtifact(item: ItemStack) {
            HibernateSession.session.beginTransaction()
            val artifact = Artifact()
            artifact.linkedItemStack = item
            HibernateSession.session.persist(artifact)
            val meta = item.itemMeta
            CustomNamespacesManager.int[CustomNamespaces.ARTIFACT_UUID]!!.setData(meta, artifact.id!!.toInt())
            item.itemMeta = meta
            HibernateSession.session.transaction.commit()
        }
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(columnDefinition = "varbinary(500)")
    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    @Column(columnDefinition = "varbinary(500)")
    @Convert(converter = ItemStackConverter::class)
    var effectRune: ItemStack = defaultItemStack

    @Column(columnDefinition = "varbinary(500)")
    @Convert(converter = ItemStackConverter::class)
    var inputRune: ItemStack = defaultItemStack

    var cooldown: Duration = Duration.ZERO
    var lastUsage: Instant? = null

    var conductivity: Float = .5f

    // Debuff logic down here
    var levelToUse: Int = 0
    var debuffStrength: Float = 1f
    var debuffType: ArtifactDebuffs = ArtifactDebuffs.NONE


    @Transient
    var cooldownDebuff: Duration = Duration.ZERO

    // Do not make this value go over 1f to ensures good behavior
    @Transient
    var conductivityDebuff: Float = 1f

    fun activate(caster: Caster, inFlow: Float): Float {

        cooldownDebuff = Duration.ZERO
        conductivityDebuff = 1f

        if (caster.getCasterLevel() < levelToUse) {
            debuffType.applyDebuff(this, caster.getCasterLevel())
        }

        val currentFlow = inFlow * conductivity * conductivityDebuff


        if (lastUsage != null && Duration.between(lastUsage, Instant.now()) < cooldown) {
            return currentFlow
        }
        lastUsage = Instant.now()

        val input = ArtifactInput(currentFlow)
        ArtifactRuneTypes.GENERIC_INPUT_RUNE.enrichArtifactInput(inputRune, caster, input, mutableListOf())

        ArtifactRuneTypes.GENERIC_EFFECT_RUNE.triggerArtifactEffect(effectRune, input, caster.getSelf() as Player)

        return 0f
    }

    fun generateEditorMenu(): StoringMenu {
        val inventorySize = 1 * 9
        val effectSlot = 8
        val inputSlot = 0

        val fillingSlots = (0 until inventorySize) - effectSlot - inputSlot
        val slots: Set<Int> = setOf(effectSlot, inputSlot)
        val editor = object : StoringMenu(slots, true) {
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Artifact Edition"))
                inventory.setItem(effectSlot, effectRune)
                inventory.setItem(inputSlot, inputRune)

                val fillingItem =
                    Utils.buildItemStack(
                        Component.text(""),
                        Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                        684867154
                    ) // Just put a unique flag here
                fillingSlots.forEach { inventory.setItem(it, fillingItem) }

                return inventory
            }

            override fun close(player: Player) {
                HibernateSession.session.beginTransaction()
                inputRune = player.openInventory.topInventory.getItem(inputSlot) ?: defaultItemStack
                effectRune = player.openInventory.topInventory.getItem(effectSlot) ?: defaultItemStack
                HibernateSession.session.transaction.commit()
            }
        }

        return editor
    }
}