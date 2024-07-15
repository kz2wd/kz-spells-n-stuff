package com.cludivers.kz2wdprison.namespaces

import org.bukkit.persistence.PersistentDataType

abstract class IntCustomNamespace : CustomNamespace<Int, Int>() {
    override val dataType: PersistentDataType<Int, Int>
        get() = PersistentDataType.INTEGER

}