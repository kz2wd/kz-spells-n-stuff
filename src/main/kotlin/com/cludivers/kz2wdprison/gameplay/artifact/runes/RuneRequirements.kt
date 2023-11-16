package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput

enum class RuneRequirements {
    NONE {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            // empty :D
        }
    },
    ENTITY {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            if (!input.enableRequirements) return
            if (input.entities.isEmpty()) {
                input.entities.add(activator.getSelf())
            }
        }
    },
    LOCATION {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            if (!input.enableRequirements) return
            if (input.locations.isNotEmpty()) return
            if (input.entities.isNotEmpty()) {
                input.locations.add(input.entities.last().location)
            } else {
                input.locations.add(activator.getSelf().location)
            }
        }
    },
    DIRECTION {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            if (!input.enableRequirements) return
            if (input.directions.isNotEmpty()) return
            if (input.entities.isNotEmpty()) {
                input.directions.add(input.entities.last().location.direction)
            } else {
                input.directions.add(activator.getSelf().location.direction)
            }
        }
    },
    LOCATION_DIRECTION {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            LOCATION.ensureRequirement(input, activator)
            DIRECTION.ensureRequirement(input, activator)
        }
    };

    abstract fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator)
}