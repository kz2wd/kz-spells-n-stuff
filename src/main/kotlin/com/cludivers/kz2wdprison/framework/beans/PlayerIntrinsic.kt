package com.cludivers.kz2wdprison.framework.beans

import jakarta.persistence.Embeddable

@Embeddable
class PlayerIntrinsic {

    var health: Float = 1f
    var speed: Float = 1f
    var armor: Float = 1f

    /**
     * If you add new attributes, remember to add them here
     */
    fun attributesSum(): Float{
        return health + speed + armor
    }

}