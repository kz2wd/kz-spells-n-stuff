package com.cludivers.kz2wdprison.modules.nation.beans

import com.cludivers.kz2wdprison.modules.shardsworld.rules.FactionsName
import com.cludivers.kz2wdprison.modules.shardsworld.rules.FactionsRulesPercentage
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class FactionRuleKey {
    @Enumerated(EnumType.STRING)
    var factionRule: FactionsRulesPercentage? = null

    @Enumerated(EnumType.STRING)
    var factionsName: FactionsName? = null


    constructor(factionRule: FactionsRulesPercentage?, factionsName: FactionsName?) {
        this.factionRule = factionRule
        this.factionsName = factionsName
    }



    constructor()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FactionRuleKey

        if (factionRule != other.factionRule) return false
        if (factionsName != other.factionsName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = factionRule?.hashCode() ?: 0
        result = 31 * result + (factionsName?.hashCode() ?: 0)
        return result
    }


}