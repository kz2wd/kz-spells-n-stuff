package com.cludivers.kz2wdprison.gameplay.artifact

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.gameplay.artifact.beans.Artifact
import com.cludivers.kz2wdprison.gameplay.artifact.beans.ArtifactComplexRune
import com.cludivers.kz2wdprison.gameplay.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.inputs.BasicInputRunes
import com.cludivers.kz2wdprison.gameplay.attributes.AttributeItem
import com.cludivers.kz2wdprison.gameplay.attributes.IntrinsicAttributes
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class DefaultArtifacts {
    LIGHTING_SWORD{
        override fun buildArtifact(): Artifact {
            val artifact: Artifact = Artifact.createArtifact(Utils.buildItemStack(Component.text("Epée éclair"), Material.IRON_SWORD), ArtifactTriggers.ATTACKING)
            val inputRune = ArtifactComplexRune.createComplexRune(CustomShardItems.COMPLEX_INPUT_RUNE.itemStack.clone(), ArtifactRuneTypes.GENERIC_INPUT_RUNE)
            PluginConfiguration.session.beginTransaction()
            inputRune.stockedItemStack = mapOf(
                1 to BasicInputRunes.ATTACKED.itemStack,
                2 to BasicInputRunes.ENTITIES_POSITION.itemStack)
            artifact.inputRune = inputRune.linkedItemStack!!
            artifact.effectRune = CustomShardItems.LIGHTNING_SPARK.itemStack
            PluginConfiguration.session.transaction.commit()
            return artifact
        }

    },
    PICKAXE{
        override fun buildArtifact(): Artifact {
            val artifact: Artifact = Artifact.createArtifact(Utils.buildItemStack(Component.text("Bonne pioche"), Material.GOLDEN_PICKAXE), ArtifactTriggers.CLICK)
            PluginConfiguration.session.beginTransaction()
            artifact.inputRune = BasicInputRunes.LOCATION_SIGHT.itemStack
            artifact.effectRune = ItemStack(Material.DIAMOND_PICKAXE)
            PluginConfiguration.session.transaction.commit()
            return artifact
        }

    },
    MAGIC_WAND{
        override fun buildArtifact(): Artifact {
            val artifact: Artifact = Artifact.createArtifact(Utils.buildItemStack(Component.text("Baguette magique"), Material.BLAZE_ROD), ArtifactTriggers.CLICK)
            val inputRune = ArtifactComplexRune.createComplexRune(CustomShardItems.COMPLEX_INPUT_RUNE.itemStack.clone(), ArtifactRuneTypes.GENERIC_INPUT_RUNE)
            PluginConfiguration.session.beginTransaction()
            inputRune.stockedItemStack = mapOf(
                0 to BasicInputRunes.ENTITY_SIGHT.itemStack,
                1 to BasicInputRunes.ENTITIES_POSITION.itemStack,
                2 to BasicInputRunes.POSITIONS_ABOVE.itemStack.asQuantity(5),
                3 to BasicInputRunes.DOWN_DIRECTION.itemStack)

            artifact.inputRune = inputRune.linkedItemStack!!
            artifact.effectRune = ItemStack(Material.ARROW)
            PluginConfiguration.session.transaction.commit()
            return artifact
        }
    },
    JUMP_FEATHER{
        override fun buildArtifact(): Artifact {
            val artifact: Artifact = Artifact.createArtifact(Utils.buildItemStack(Component.text("Saut magique"), Material.FEATHER), ArtifactTriggers.CLICK)
            val inputRune = ArtifactComplexRune.createComplexRune(CustomShardItems.COMPLEX_INPUT_RUNE.itemStack.clone(), ArtifactRuneTypes.GENERIC_INPUT_RUNE)
            PluginConfiguration.session.beginTransaction()
            inputRune.stockedItemStack = mapOf(
                0 to BasicInputRunes.ENTITY_CASTER.itemStack,
                1 to BasicInputRunes.ENTITIES_DIRECTION.itemStack)

            artifact.inputRune = inputRune.linkedItemStack!!
            artifact.effectRune = CustomShardItems.MOVE_RUNE.itemStack
            PluginConfiguration.session.transaction.commit()
            return artifact
        }
    },
    FIRE_HELMET{
        override fun buildArtifact(): Artifact {
            val itemstack = Utils.buildItemStack(Component.text("Casque de feu"), Material.LEATHER_HELMET)
            val artifact: Artifact = Artifact.createArtifact(itemstack, ArtifactTriggers.ATTACKED)
            PluginConfiguration.session.beginTransaction()
            artifact.inputRune = BasicInputRunes.ATTACKER.itemStack
            artifact.effectRune = CustomShardItems.FIRE_SPARK.itemStack
            PluginConfiguration.session.transaction.commit()
            val intrinsics = mapOf(IntrinsicAttributes.AGILITY to .3)
            AttributeItem.makeAttributeItem(itemstack, intrinsics, 500, 800)
            return artifact
        }
    },
    KNOCK_CHEST_PLATE {
        override fun buildArtifact(): Artifact {
            val itemstack = Utils.buildItemStack(Component.text("Pousse-Plastron"), Material.IRON_CHESTPLATE)
            val artifact: Artifact = Artifact.createArtifact(itemstack, ArtifactTriggers.ATTACKED)
            val inputRune = ArtifactComplexRune.createComplexRune(CustomShardItems.COMPLEX_INPUT_RUNE.itemStack.clone(), ArtifactRuneTypes.GENERIC_INPUT_RUNE)
            PluginConfiguration.session.beginTransaction()
            inputRune.stockedItemStack = mapOf(
                0 to BasicInputRunes.ATTACKER.itemStack,
                1 to BasicInputRunes.ENTITIES_DIRECTION.itemStack,
                2 to BasicInputRunes.INVERT_DIRECTION.itemStack)

            artifact.inputRune = inputRune.linkedItemStack!!
            artifact.effectRune = CustomShardItems.MOVE_RUNE.itemStack
            PluginConfiguration.session.transaction.commit()

            val intrinsics = mapOf(
                IntrinsicAttributes.TOUGHNESS to .3,
                IntrinsicAttributes.VIGOR to .5)
            AttributeItem.makeAttributeItem(itemstack, intrinsics, 2000, 2500)
            return artifact
        }
    },
    FEEDER_BOOTS{
        override fun buildArtifact(): Artifact {
            val artifact: Artifact = Artifact.createArtifact(
                Utils.buildItemStack(Component.text("Bottes nourrisseuses"),
                Material.GOLDEN_BOOTS), ArtifactTriggers.ATTACKED)
            PluginConfiguration.session.beginTransaction()
            artifact.inputRune = BasicInputRunes.ATTACKED.itemStack
            artifact.effectRune = ItemStack(Material.GOLDEN_APPLE)
            PluginConfiguration.session.transaction.commit()
            return artifact
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