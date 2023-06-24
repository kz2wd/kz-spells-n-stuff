package com.cludivers.kz2wdprison.gameplay.attributes.namespaces

import org.bukkit.persistence.PersistentDataType

abstract class DoubleCustomNamespace : CustomNamespace<Double, Double>() {
    override val dataType: PersistentDataType<Double, Double>
        get() = PersistentDataType.DOUBLE
}