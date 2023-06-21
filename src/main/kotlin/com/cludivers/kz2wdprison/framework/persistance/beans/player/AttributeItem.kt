package com.cludivers.kz2wdprison.framework.persistance.beans.player

import com.cludivers.kz2wdprison.framework.persistance.converters.ItemStackConverter
import jakarta.persistence.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hibernate.Session
import kotlin.math.min

@Entity
class AttributeItem {

    @Id
    @GeneratedValue
    var id: Long? = null

    var shardPowerStored: Int = 0
    var maxShardPower: Int = 100


    @ElementCollection
    var attributesRepartition: MutableMap<IntrinsicAttributes, Int> = mutableMapOf()


    @Convert(converter = ItemStackConverter::class)
    var linkedItemStack: ItemStack? = null

    companion object {
        val AttributeItems: MutableMap<ItemStack, AttributeItem> = mutableMapOf()

        fun registerAttributeItem(item: AttributeItem, itemStack: ItemStack) {
            AttributeItems[itemStack] = item
        }

        fun isItemStackLinked(item: ItemStack): Boolean {
            return AttributeItems.containsKey(item)
        }

        fun initPersistentArtifactComplexRune(session: Session) {
            val artifactComplexInputs =
                session.createQuery("from AttributeItem A", AttributeItem::class.java).list()
            artifactComplexInputs.filter { it.linkedItemStack != null }
                .forEach { registerAttributeItem(it, it.linkedItemStack!!) }
        }

    }


    /**
     * WARNING : need to be in transaction
     * Do not use this function with negative number
     *
     * Add shard power into the player system,
     *
     * @return amount of shards in excess
     */
    fun addShardPower(player: Player, playerData: PlayerBean, addedShardPower: Int): Int {
        val amountAvailable = maxShardPower - shardPowerStored
        val addedAmount = min(amountAvailable, addedShardPower)
        shardPowerStored += addedAmount

        increasePlayerAttributes(player, playerData, addedAmount)

        return addedShardPower - addedAmount
    }

    private fun increasePlayerAttributes(player: Player, playerData: PlayerBean, addedAmount: Int) {
        val sum = attributesRepartition.values.sum()

        attributesRepartition.forEach {
            player.getAttribute(it.key.relatedAttribute)?.baseValue =
                player.getAttribute(it.key.relatedAttribute)?.baseValue?.plus(
                    it.key.intrinsicToGenericValue(it.value / sum * addedAmount)
                )!!
        }
        playerData.intrinsic.updatePlayer(player)
    }

    private fun decreasePlayerAttributes(player: Player, playerData: PlayerBean, removedAmount: Int) {
        val sum = attributesRepartition.values.sum()

        attributesRepartition.forEach {
            player.getAttribute(it.key.relatedAttribute)?.baseValue =
                player.getAttribute(it.key.relatedAttribute)?.baseValue?.minus(
                    it.key.intrinsicToGenericValue(it.value / sum * removedAmount)
                )!!
        }
        playerData.intrinsic.updatePlayer(player)
    }


    /**
     * WARNING : need to be in transaction
     * Do not use this function with negative number
     */
    fun reduceShardPower(player: Player, playerData: PlayerBean, removedShardPower: Int) {
        val reductionAmount = min(shardPowerStored, removedShardPower)
        shardPowerStored -= reductionAmount

        decreasePlayerAttributes(player, playerData, reductionAmount)

    }

    fun isFull(): Boolean {
        return shardPowerStored >= maxShardPower
    }

    fun equip(player: Player, playerData: PlayerBean) {
        increasePlayerAttributes(player, playerData, shardPowerStored)
    }

    fun unequip(player: Player, playerData: PlayerBean) {
        decreasePlayerAttributes(player, playerData, shardPowerStored)
    }

}