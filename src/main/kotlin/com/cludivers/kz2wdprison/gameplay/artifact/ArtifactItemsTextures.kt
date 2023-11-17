package com.cludivers.kz2wdprison.gameplay.artifact

enum class ArtifactItemsTextures {
    SHARDS {
        override val customData: Int = 10000
    },
    FIRE_SPARK {
        override val customData: Int = 10001
    },
    LIGHTNING_SPARK {
        override val customData: Int = 10002
    },
    UP_RUNE {
        override val customData: Int = 10003
    },
    DOWN_RUNE {
        override val customData: Int = 10004
    },
    LEFT_RUNE {
        override val customData: Int = 10005
    },
    RIGHT_RUNE {
        override val customData: Int = 10006
    },
    FRONT_RUNE {
        override val customData: Int = 10007
    },
    BACK_RUNE {
        override val customData: Int = 10008
    },
    MOVE_RUNE {
        override val customData: Int = 10009
    },
    ENTITY_CASTER {
        override val customData: Int = 5000
    },
    ENTITY_SIGHT {
        override val customData: Int = 5001
    },
    LOCATION_SIGHT {
        override val customData: Int = 5003
    },
    EMPTY_LOCATION_SIGHT {
        override val customData: Int = 5004
    },
    PROJECTILE_CASTING {
        override val customData: Int = 5005
    },
    CASTER_DIRECTION {
        override val customData: Int = 5006
    },
    ENTITIES_LOCATION {
        override val customData: Int = 6000
    },
    ENTITIES_DIRECTION {
        override val customData: Int = 6004
    },
    LOCATIONS_BELOW {
        override val customData: Int = 6005
    },
    LOCATIONS_ABOVE {
        override val customData: Int = 6006
    },
    LOCATIONS_IN_FRONT {
        override val customData: Int = 6007
    },
    LOCATION_AROUND_FLAT {
        override val customData: Int = 6008
    },
    DOWN_DIRECTION {
        override val customData: Int = 6009
    },
    UP_DIRECTION {
        override val customData: Int = 6010
    },
    INVERT_DIRECTION {
        override val customData: Int = 6011
    },
    DOUBLE_DIRECTION {
        override val customData: Int = 6012
    },
    HALF_DIRECTION {
        override val customData: Int = 6013
    },
    ENTITIES_AROUND {
        override val customData: Int = 6014
    },
    NONE {
        override val customData: Int = 4999
    },
    LOCATIONS_BEHIND {
        override val customData: Int = 6015
    },
    LOCATION_AROUND {
        override val customData: Int = 6016
    },
    CASTER_PROJECTILE {
        override val customData: Int = 6017
    }
    ;

    abstract val customData: Int
}