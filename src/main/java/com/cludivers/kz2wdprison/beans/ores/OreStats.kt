package com.cludivers.kz2wdprison.beans.ores

import jakarta.persistence.Embeddable

@Embeddable
class OreStats(var maxXp: Int = 0) {
    var amountMined = 0
}