package com.cludivers.kz2wdprison.attributes

import com.cludivers.kz2wdprison.namespaces.CustomNamespaces
import com.cludivers.kz2wdprison.namespaces.CustomNamespacesManager
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.math.min
import kotlin.random.Random


class AttributeItem {
    companion object {

        val shardPowerRatio = .01

        fun makeAttributeItem(itemStack: ItemStack, baseShards: Int = 0, maxShards: Int = 1000) {
            val attributes = IntrinsicAttributes.values().mapNotNull {
                if (Random.nextBoolean()) it to Random.nextDouble() else null
            }.toMap()
            makeAttributeItem(itemStack, attributes, baseShards, maxShards)
        }

        fun makeAttributeItem(itemStack: ItemStack, attributes: Map<IntrinsicAttributes, Double>, baseShards: Int = 0, maxShards: Int = 1000) {
            val meta = itemStack.itemMeta

            attributes
            .map {
                CustomNamespacesManager.float[it.key]!! to it.value
            }
            .forEach {
                it.first.setData(meta, it.second)
            }
            CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.setData(meta, baseShards)
            CustomNamespacesManager.int[CustomNamespaces.MAX_SHARDS_STORAGE]!!.setData(meta, maxShards)

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

            itemStack.itemMeta = meta
            updateItemAttributes(itemStack)
        }

        private fun updateItemAttributes(item: ItemStack) {
            val meta = item.itemMeta
            IntrinsicAttributes.values().forEach { meta.removeAttributeModifier(it.relatedAttribute) }
            val shardPowerStored = CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.getData(meta) ?: return
            IntrinsicAttributes.values().map {
                it to CustomNamespacesManager.float[it]!!
            }
                .map {
                    it.first to it.second.getData(meta)
                }
                .filter {
                    it.second != null
                }
                .forEach {
                    meta.addAttributeModifier(
                        it.first.relatedAttribute,
                        AttributeModifier(
                            it.first.relatedAttribute.translationKey(),
                            it.first.intrinsicToGenericValue(it.second!! * shardPowerStored * shardPowerRatio),
                            AttributeModifier.Operation.ADD_NUMBER
                        )
                    )
                }

            item.itemMeta = meta

            updateItemStackDescription(item)
        }

        fun addShards(item: ItemStack, addedShardPower: Int): Int {
            val meta = item.itemMeta
            val shardPowerStored =
                CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.getData(meta) ?: return addedShardPower
            val maxShardPower = CustomNamespacesManager.int[CustomNamespaces.MAX_SHARDS_STORAGE]!!.getData(meta)
                ?: return addedShardPower
            val amountAvailable = maxShardPower - shardPowerStored
            val addedAmount = min(amountAvailable, addedShardPower)
            CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.setData(meta, shardPowerStored + addedAmount)

            item.itemMeta = meta

            updateItemAttributes(item)

            return addedShardPower - addedAmount
        }

        fun removeShards(item: ItemStack, removedShardPower: Int) {
            val meta = item.itemMeta
            val shardPowerStored = CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.getData(meta) ?: return
            val reductionAmount = min(shardPowerStored, removedShardPower)
            CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.setData(
                meta,
                shardPowerStored - reductionAmount
            )
            item.itemMeta = meta
            updateItemAttributes(item)
        }

        private fun updateItemStackDescription(item: ItemStack) {
            if (item.itemMeta == null) {
                return
            }
            val meta = item.itemMeta
            val shardPowerStored = CustomNamespacesManager.int[CustomNamespaces.SHARDS_STORED]!!.getData(meta) ?: return
            val maxShardPower =
                CustomNamespacesManager.int[CustomNamespaces.MAX_SHARDS_STORAGE]!!.getData(meta) ?: return
            val line = Component.text("${ChatColor.ITALIC}${ChatColor.DARK_PURPLE}- - - - - - - - -")
            val shardsAmount =
                Component.text("${ChatColor.GRAY}Shards : ${ChatColor.GREEN}${shardPowerStored} / ${maxShardPower}")
//            val itemRank = Component.text("${ChatColor.GRAY}Rang : ").append(RateLetters.getAttributeRateLetter(baseScaling * 10))
            val scalings = IntrinsicAttributes.values().map {
                it to CustomNamespacesManager.float[it]!!.getData(meta)
            }.filter { it.second != null }
                .map {
                    Component.text("${ChatColor.GRAY}${it.first.text} : ").append(
                        RateLetters.getAttributeRateLetter(
                            it.second!!
                        )
                    )
                }
            val lore = listOf(shardsAmount, line) + scalings
            item.lore(lore)
        }

    }
}