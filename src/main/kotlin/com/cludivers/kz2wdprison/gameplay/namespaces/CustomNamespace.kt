package com.cludivers.kz2wdprison.gameplay.namespaces

import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

abstract class CustomNamespace<T, Z : Any> {

    open lateinit var namespacedKey: NamespacedKey

    abstract val dataType: PersistentDataType<T, Z>

    fun getData(meta: ItemMeta): Z? {
        return meta.persistentDataContainer.get(namespacedKey, dataType)
    }

    fun setData(meta: ItemMeta, data: Z) {
        meta.persistentDataContainer.set(namespacedKey, dataType, data)
    }


}