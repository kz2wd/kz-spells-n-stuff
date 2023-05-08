package com.cludivers.kz2wdprison.framework.persistance.converters

import jakarta.persistence.AttributeConverter
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackConverter: AttributeConverter<ItemStack, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ItemStack?): ByteArray {
        return if (attribute != null) {
            attribute.serializeAsBytes()
        } else {
            ByteArray(0)
        }
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): ItemStack {
        return if (dbData != null){
            ItemStack.deserializeBytes(dbData)
        } else {
            ItemStack(Material.AIR)
        }
    }
}