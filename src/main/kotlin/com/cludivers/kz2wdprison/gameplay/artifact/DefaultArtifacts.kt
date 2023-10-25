package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.gameplay.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.beans.Artifact
import com.cludivers.kz2wdprison.gameplay.attributes.AttributeItem
import com.cludivers.kz2wdprison.gameplay.attributes.IntrinsicAttributes
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

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
                    CustomShardItems.ENTITIES_POSITION_RUNE.itemStack,
                    CustomShardItems.LIGHTNING_SPARK.itemStack,
                )
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
                    CustomShardItems.LOCATION_SIGHT_RUNE.itemStack.asQuantity(10),
                    ItemStack(Material.DIAMOND_PICKAXE)
                )
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
                    CustomShardItems.ENTITIES_IN_SIGHT_RUNE.itemStack.asQuantity(10),
                    CustomShardItems.ENTITIES_POSITION_RUNE.itemStack,
                    CustomShardItems.LOCATIONS_ABOVE_RUNE.itemStack.asQuantity(5),
                    CustomShardItems.DOWN_DIRECTION_RUNE.itemStack,
                    ItemStack(Material.ARROW)

                )
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
                    CustomShardItems.ENTITY_CASTER_RUNE.itemStack,
                    CustomShardItems.ENTITIES_DIRECTION_RUNE.itemStack,
                    CustomShardItems.MOVE_RUNE.itemStack
                )
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
                )
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
                    CustomShardItems.INVERT_DIRECTIONS_RUNE.itemStack,
                    CustomShardItems.MOVE_RUNE.itemStack
                )
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
                )
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