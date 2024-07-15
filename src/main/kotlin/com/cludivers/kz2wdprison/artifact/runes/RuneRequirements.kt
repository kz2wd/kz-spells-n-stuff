package com.cludivers.kz2wdprison.artifact.runes

import com.cludivers.kz2wdprison.artifact.ArtifactExecution

enum class RuneRequirements {
    NONE {
        override fun ensureRequirement(execution: ArtifactExecution) {
            // empty :D
        }
    },
    ENTITY {
        override fun ensureRequirement(execution: ArtifactExecution) {
            if (!execution.enableRequirements) return
            if (execution.entities.isEmpty()) {
                execution.entities.add(execution.activator.getSelf())
            }
        }
    },
    LOCATION {
        override fun ensureRequirement(execution: ArtifactExecution) {
            if (!execution.enableRequirements) return
            if (execution.locations.isNotEmpty()) return
            if (execution.entities.isNotEmpty()) {
                execution.locations.add(execution.entities.last().location)
            } else {
                execution.locations.add(execution.activator.getSelf().location)
            }
        }
    },
    DIRECTION {
        override fun ensureRequirement(execution: ArtifactExecution) {
            if (!execution.enableRequirements) return
            if (execution.directions.isNotEmpty()) return
            if (execution.entities.isNotEmpty()) {
                execution.directions.add(execution.entities.last().location.direction)
            } else {
                execution.directions.add(execution.activator.getSelf().location.direction)
            }
        }
    },
    LOCATION_DIRECTION {
        override fun ensureRequirement(execution: ArtifactExecution) {
            LOCATION.ensureRequirement(execution)
            DIRECTION.ensureRequirement(execution)
        }
    };

    abstract fun ensureRequirement(execution: ArtifactExecution)
}