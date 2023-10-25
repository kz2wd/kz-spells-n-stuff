package com.cludivers.kz2wdprison.framework.persistence.beans.player

import com.cludivers.kz2wdprison.gameplay.attributes.IntrinsicAttributes
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable

@Embeddable
class PlayerIntrinsic {

    @ElementCollection
    var attributes: MutableMap<IntrinsicAttributes, Int> =
        IntrinsicAttributes.values().associateWith { it.baseValue }.toMutableMap()

    @ElementCollection
    var skills: MutableMap<PlayerSkills, Int> = mutableMapOf()

}