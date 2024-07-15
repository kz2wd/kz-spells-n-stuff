package com.cludivers.kz2wdprison.persistence.converters

import jakarta.persistence.AttributeConverter
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackConverter: AttributeConverter<ItemStack, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ItemStack?): ByteArray {
        // Air cannot be serialized so filter it !
        return if (attribute != null && attribute.type != Material.AIR) {
            attribute.serializeAsBytes()
        } else {
            ByteArray(0)
        }
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): ItemStack {
        return if (dbData != null) {
            return if (dbData.isEmpty()) {
                ItemStack(Material.AIR)
            } else {
                ItemStack.deserializeBytes(dbData)
            }
        } else {
            ItemStack(Material.AIR)
        }
    }
}