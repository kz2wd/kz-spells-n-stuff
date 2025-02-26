package com.cludivers.kz2wdprison.modules.nation.beans

import java.time.Duration

enum class ProtectionTiers {
    TIER1 {
        override val attackDuration: Duration
            get() = Duration.ofDays(2)
        override val attackRatio: Double
            get() = .5
    },
    TIER2 {
        override val attackDuration: Duration
            get() = Duration.ofDays(1)
        override val attackRatio: Double
            get() = 1.0
    },
    TIER3 {
        override val attackDuration: Duration
            get() = Duration.ofHours(10)
        override val attackRatio: Double
            get() = 2.0
    },
    TIER4 {
        override val attackDuration: Duration
            get() = Duration.ofHours(5)
        override val attackRatio: Double
            get() = 3.0
    },
    TIER5 {
        override val attackDuration: Duration
            get() = Duration.ZERO
        override val attackRatio: Double
            get() = 5.0
    };

    abstract val attackDuration: Duration
    abstract val attackRatio: Double


}