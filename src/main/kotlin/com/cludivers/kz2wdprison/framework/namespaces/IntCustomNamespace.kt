package com.cludivers.kz2wdprison.framework.namespaces

import org.bukkit.persistence.PersistentDataType

abstract class IntCustomNamespace : CustomNamespace<Int, Int>() {
    override val dataType: PersistentDataType<Int, Int>
        get() = PersistentDataType.INTEGER

}