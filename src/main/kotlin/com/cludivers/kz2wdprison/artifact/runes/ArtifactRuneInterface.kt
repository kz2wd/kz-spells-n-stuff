package com.cludivers.kz2wdprison.artifact.runes

import com.cludivers.kz2wdprison.artifact.ArtifactExecution

interface ArtifactRuneInterface {

    // The player is only needed for block placing or destroying event, to get permissions
    fun processArtifactActivation(
        execution: ArtifactExecution
    )

    fun triggerNext(execution: ArtifactExecution)

    fun addDuration(input: ArtifactExecution)

}