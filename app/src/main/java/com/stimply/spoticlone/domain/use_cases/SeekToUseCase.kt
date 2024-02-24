package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.service.PlayerController

class SeekToUseCase(
    private val controller: PlayerController
) {
    operator fun invoke(position: Long) {
        controller.whenReady {
            controller.seekTo(position)
        }
    }
}