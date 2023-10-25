package com.cludivers.kz2wdprison.framework.persistence.beans.ores

import jakarta.persistence.Embeddable

@Embeddable
class OreStats(var maxXp: Float = 0f) {
    var amountMined = 0
}