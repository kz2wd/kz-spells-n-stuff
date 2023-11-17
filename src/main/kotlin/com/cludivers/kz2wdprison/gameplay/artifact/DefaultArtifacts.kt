package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.beans.Artifact
import com.cludivers.kz2wdprison.gameplay.attributes.AttributeItem
import com.cludivers.kz2wdprison.gameplay.attributes.IntrinsicAttributes
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.time.Duration

enum class DefaultArtifacts {
    LIGHTING_SWORD{
        override fun buildArtifact(): Artifact {
            return Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Epée éclair"),
                    Material.IRON_SWORD
                ),
                ArtifactTriggers.ATTACKING,
                listOf(
                    CustomShardItems.ENTITY_ATTACKED_RUNE.itemStack,
                    CustomShardItems.ENTITIES_LOCATION_RUNE.itemStack,
                    CustomShardItems.LIGHTNING_SPARK.itemStack,
                ),
                Duration.ofSeconds(2)
            )
        }

    },
    PICKAXE{
        override fun buildArtifact(): Artifact {
            return Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Bonne pioche"),
                    Material.GOLDEN_PICKAXE
                ),
                ArtifactTriggers.CLICK,
                listOf(
                    CustomShardItems.CASTER_PROJECTILE_RUNE.itemStack,
                    CustomShardItems.DOUBLE_DIRECTION_RUNE.itemStack,
                    ItemStack(Material.ARROW),
                    CustomShardItems.LOCATION_AROUND_FLAT_RUNE.itemStack.asQuantity(2),
                    CustomShardItems.LIGHTNING_SPARK.itemStack.asQuantity(25),
                    ItemStack(Material.DIAMOND_PICKAXE)
                ),
                Duration.ofSeconds(2)
            )
        }

    },
    MAGIC_WAND{
        override fun buildArtifact(): Artifact {
            return Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Invoka-flêche"),
                    Material.BLAZE_ROD
                ),
                ArtifactTriggers.CLICK,
                listOf(
                    CustomShardItems.ENTITY_IN_SIGHT_RUNE.itemStack.asQuantity(10),
                    CustomShardItems.ENTITIES_LOCATION_RUNE.itemStack,
                    CustomShardItems.LOCATION_ABOVE_RUNE.itemStack.asQuantity(5),
                    CustomShardItems.DOWN_DIRECTION_RUNE.itemStack,
                    ItemStack(Material.ARROW)

                ),
                Duration.ofSeconds(2)
            )
        }
    },
    JUMP_FEATHER{
        override fun buildArtifact(): Artifact {
            return Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Saut magique"),
                    Material.FEATHER
                ),
                ArtifactTriggers.CLICK,
                listOf(
                    CustomShardItems.ENTITY_CASTER_RUNE.itemStack.asQuantity(2),
                    CustomShardItems.ENTITIES_DIRECTION_RUNE.itemStack,
                    CustomShardItems.MOVE_RUNE.itemStack
                ),
                Duration.ofSeconds(2)
            )
        }
    },
    FIRE_HELMET{
        override fun buildArtifact(): Artifact {
            val artifact: Artifact = Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Casque enflammeur"),
                    Material.LEATHER_HELMET
                ),
                ArtifactTriggers.ATTACKED,
                listOf(
                    CustomShardItems.ENTITY_ATTACKER_RUNE.itemStack,
                    ItemStack(Material.FLINT_AND_STEEL),
                ),
                Duration.ofSeconds(2)
            )
            val intrinsics = mapOf(IntrinsicAttributes.AGILITY to .3)
            AttributeItem.makeAttributeItem(artifact.linkedItemStack!!, intrinsics, 500, 800)

            return artifact
        }
    },
    KNOCK_CHEST_PLATE {
        override fun buildArtifact(): Artifact {
            val artifact = Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Pousse-Plastron"),
                    Material.IRON_CHESTPLATE
                ),
                ArtifactTriggers.ATTACKED,
                listOf(
                    CustomShardItems.ENTITY_ATTACKER_RUNE.itemStack,
                    CustomShardItems.ENTITIES_DIRECTION_RUNE.itemStack,
                    CustomShardItems.INVERT_DIRECTION_RUNE.itemStack,
                    CustomShardItems.MOVE_RUNE.itemStack
                ),
                Duration.ofSeconds(2)
            )
            val intrinsics = mapOf(
                IntrinsicAttributes.TOUGHNESS to .3,
                IntrinsicAttributes.VIGOR to .5)
            AttributeItem.makeAttributeItem(artifact.linkedItemStack!!, intrinsics, 2000, 2500)
            return artifact
        }
    },
    FEEDER_BOOTS{
        override fun buildArtifact(): Artifact {
            return Artifact.createArtifact(
                Utils.buildItemStack(
                    Component.text("Bottes nourrissantes"),
                    Material.GOLDEN_BOOTS
                ),
                ArtifactTriggers.ATTACKED,
                listOf(
                    CustomShardItems.ENTITY_ATTACKED_RUNE.itemStack,
                    ItemStack(Material.GOLDEN_APPLE),
                ),
                Duration.ofSeconds(30)
            )
        }
    },

    ;


    protected abstract fun buildArtifact(): Artifact
    lateinit var artifact: Artifact

    companion object {
        fun initDefaultArtifacts(){
            DefaultArtifacts.values().forEach { it.artifact = it.buildArtifact() }
        }
    }

}