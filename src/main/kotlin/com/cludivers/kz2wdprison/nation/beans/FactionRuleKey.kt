package com.cludivers.kz2wdprison.nation.beans

import com.cludivers.kz2wdprison.shardsworld.rules.FactionsName
import com.cludivers.kz2wdprison.shardsworld.rules.FactionsRulesPercentage
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

    constructor(){

    }


}