package com.cludivers.kz2wdprison.modules.artifact

enum class ArtifactDebuffs {
    NONE {
        override fun applyDebuff(artifact: Artifact, playerLevel: Int) {

        }
    },
    CONDUCTIVITY {
        override fun applyDebuff(artifact: Artifact, playerLevel: Int) {
            val levelDiff = artifact.levelToUse - playerLevel
            if (levelDiff <= 0) return
            artifact.conductivityDebuff = 1f / levelDiff
        }
    },
    COOLDOWN {
        override fun applyDebuff(artifact: Artifact, playerLevel: Int) {
            val levelDiff = artifact.levelToUse - playerLevel
            if (levelDiff <= 0) return
            artifact.cooldownDebuff = artifact.cooldown.multipliedBy(levelDiff * 1L)
        }
    };

    abstract fun applyDebuff(artifact: Artifact, playerLevel: Int)


}