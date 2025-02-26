package com.cludivers.kz2wdprison.framework.namespaces

import com.cludivers.kz2wdprison.modules.attributes.IntrinsicAttributes
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

object CustomNamespacesManager {

    fun initAllNamespacedKeys(plugin: JavaPlugin) {
        int.forEach { it.value.namespacedKey = NamespacedKey(plugin, it.key.toString()) }
        float.forEach { it.value.namespacedKey = NamespacedKey(plugin, it.key.toString()) }
    }

    val int = mapOf(
        CustomNamespaces.ARTIFACT_UUID to object : IntCustomNamespace() {},
        CustomNamespaces.COMPLEX_RUNE_UUID to object : IntCustomNamespace() {},
        CustomNamespaces.SHARDS_STORED to object : IntCustomNamespace() {},
        CustomNamespaces.MAX_SHARDS_STORAGE to object : IntCustomNamespace() {},
    )

    val float = mapOf(
        IntrinsicAttributes.VIGOR to object : DoubleCustomNamespace() {},
        IntrinsicAttributes.AGILITY to object : DoubleCustomNamespace() {},
        IntrinsicAttributes.DEXTERITY to object : DoubleCustomNamespace() {},
        IntrinsicAttributes.TOUGHNESS to object : DoubleCustomNamespace() {},
        IntrinsicAttributes.STRENGTH to object : DoubleCustomNamespace() {}
    )

}