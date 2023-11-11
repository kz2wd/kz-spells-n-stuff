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
                input.entities = listOf(activator.getSelf())
            }
        }
    },
    LOCATION {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            if (!input.enableRequirements) return
            if (input.locations.isNotEmpty()) return
            if (input.entities.isNotEmpty()) {
                input.locations = input.entities.map { it.location }
            } else {
                input.locations = listOf(activator.getSelf().location)
            }
        }
    },
    DIRECTION {
        override fun ensureRequirement(input: ArtifactInput, activator: ArtifactActivator) {
            if (!input.enableRequirements) return
            if (input.directions.isNotEmpty()) return
            if (input.entities.isNotEmpty()) {
                input.directions = input.entities.map { it.location.direction }
            } else {
                input.directions = listOf(activator.getSelf().location.direction)
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