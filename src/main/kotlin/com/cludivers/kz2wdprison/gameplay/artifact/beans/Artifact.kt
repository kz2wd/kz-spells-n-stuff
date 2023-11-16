package com.cludivers.kz2wdprison.gameplay.artifact.beans

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.framework.persistence.converters.ItemStackConverter
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactDebuffs
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactTriggers
import com.cludivers.kz2wdprison.gameplay.artifact.runes.RunesBehaviors
import com.cludivers.kz2wdprison.gameplay.menu.StoringMenu
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespaces
import com.cludivers.kz2wdprison.gameplay.namespaces.CustomNamespacesManager
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.time.Instant
import kotlin.jvm.Transient
import kotlin.math.abs

@Entity
class Artifact {

    companion object {
        val defaultItemStack = ItemStack(Material.AIR)

        fun getArtifact(itemStack: ItemStack): Artifact? {
            if (itemStack.itemMeta == null) return null
            val uuid =
                CustomNamespacesManager.int[CustomNamespaces.ARTIFACT_UUID]!!.getData(itemStack.itemMeta) ?: return null
            return PluginConfiguration.session
                .createQuery("from Artifact A where A.id = :uuid", Artifact::class.java)
                .setParameter("uuid", uuid.toString())
                .uniqueResult()
        }

        fun deleteArtifact(artifact: Artifact) {
            PluginConfiguration.session.remove(artifact)
        }

        fun createArtifact(
            item: ItemStack,
            triggerType: ArtifactTriggers = ArtifactTriggers.CLICK,
            runes: List<ItemStack>? = null,
            coolDown: Duration = Duration.ZERO

        ): Artifact {
            PluginConfiguration.session.beginTransaction()
            val artifact = Artifact()
            artifact.runes = runes?.withIndex()?.associate { it.index to it.value } ?: mapOf()
            artifact.cooldown = coolDown
            artifact.linkedItemStack = item
            artifact.triggerType = triggerType
            PluginConfiguration.session.persist(artifact)
            val meta = item.itemMeta
            CustomNamespacesManager.int[CustomNamespaces.ARTIFACT_UUID]!!.setData(meta, artifact.id!!.toInt())
            item.itemMeta = meta
            PluginConfiguration.session.transaction.commit()
            return artifact
        }
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(columnDefinition = "varbinary(1000)")
    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    @ElementCollection(targetClass = ItemStack::class)
    @Convert(attributeName = "value", converter = ItemStackConverter::class)
    var runes: Map<Int, ItemStack> = mapOf()

    var cooldown: Duration = Duration.ZERO
    var lastUsage: Instant? = null

    var conductivity: Float = .5f

    // Debuff logic down here
    var levelToUse: Int = 0
    var debuffStrength: Float = 1f
    var debuffType: ArtifactDebuffs = ArtifactDebuffs.NONE

    var triggerType: ArtifactTriggers = ArtifactTriggers.CLICK

    @Transient
    var cooldownDebuff: Duration = Duration.ZERO

    // Do not make this value go over 1f to ensures good behavior
    @Transient
    var conductivityDebuff: Float = 1f

    fun activate(artifactActivator: ArtifactActivator, inFlow: Float, triggerType: ArtifactTriggers): Float {
        if (triggerType != this.triggerType) return inFlow

        cooldownDebuff = Duration.ZERO
        conductivityDebuff = 1f

        if (artifactActivator.getCasterLevel() < levelToUse) {
            debuffType.applyDebuff(this, artifactActivator.getCasterLevel())
        }

        val currentFlow = inFlow * conductivity * conductivityDebuff

        if (lastUsage != null) {
            val remainingReloadTime = Duration.between(lastUsage, Instant.now()) - cooldown
            if (remainingReloadTime.isNegative) {
                val activator = artifactActivator.getSelf()
                if (activator is Player && triggerType == ArtifactTriggers.CLICK) {
                    activator.sendActionBar(
                        Component.text(" - *** ${abs(remainingReloadTime.toMillis())} *** - ").color(NamedTextColor.RED)
                            .decorate(TextDecoration.BOLD)
                    )
                    activator.location.world.playSound(activator, Sound.BLOCK_BEACON_DEACTIVATE, .3f, 1f)
                    activator.location.world.spawnParticle(Particle.ELECTRIC_SPARK, activator.location, 10)
                }
                return currentFlow
            }
        }
        lastUsage = Instant.now()

        val activator = artifactActivator.getSelf()
        if (activator is Player && triggerType == ArtifactTriggers.CLICK) {
            activator.sendActionBar(
                Component.text(" - *** ^^^^ *** - ").color(NamedTextColor.GREEN)
            )
            activator.location.world.playSound(activator, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, .3f, 1f)
            activator.location.world.spawnParticle(Particle.DRAGON_BREATH, activator.location, 10)
        }

        val input = ArtifactInput(currentFlow)
        val player = if (artifactActivator.getSelf() is Player){
            artifactActivator.getSelf() as Player
        } else {
            null
        }

        val runesOrdered = runes.filter {
            it.value.type != Material.AIR
        }.toList().sortedBy { it.first }.map { it.second }

        fun generateActivation(runeIndex: Int): ((ArtifactInput) -> ArtifactInput) -> Unit {
            if (runesOrdered.size <= runeIndex) return {
                PluginConfiguration.session.beginTransaction()
                this.cooldown = input.duration.plus(cooldownDebuff)
                PluginConfiguration.session.transaction.commit()
            }
            return fun(inputModifier: ((ArtifactInput) -> ArtifactInput)) {
                RunesBehaviors.processArtifactActivation(
                        runesOrdered[runeIndex],
                        artifactActivator,
                        inputModifier(input),
                        mutableListOf(),
                        player,
                        generateActivation(runeIndex + 1)
                )
            }
        }

        generateActivation(0)(ArtifactInput::sameInput)

        return 0f
    }


    fun generateEditorMenu(): StoringMenu {
        val inventorySize = 5 * 9
        val itemStackSlots = (0 until inventorySize - 9)
        // Keep it there, I'll need it later
//        val itemStackSlots =
//            (1 until 5).map { (1 * it until 5 * it).toList() }.reduce { acc, ints -> acc + ints } - 22

        val fillingSlots = (0 until inventorySize) - itemStackSlots
        val slots: Set<Int> = (itemStackSlots).toSet()
        val editor = object : StoringMenu(slots, true) {
            override fun generateInventory(player: Player): Inventory {
                val inventory = Bukkit.createInventory(player, inventorySize, Component.text("Edition d'artefacte"))

                runes.forEach {
                    inventory.setItem(it.key, it.value)
                }

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
                PluginConfiguration.session.beginTransaction()
                runes = itemStackSlots.associateWith {
                    player.openInventory.topInventory.getItem(it)
                }.filter { it.value != null } as Map<Int, ItemStack>
                PluginConfiguration.session.transaction.commit()
            }
        }

        return editor
    }
}