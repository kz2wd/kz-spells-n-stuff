package com.cludivers.kz2wdprison.framework.namespaces

import org.bukkit.persistence.PersistentDataType

abstract class DoubleCustomNamespace : CustomNamespace<Double, Double>() {
    override val dataType: PersistentDataType<Double, Double>
        get() = PersistentDataType.DOUBLE
}