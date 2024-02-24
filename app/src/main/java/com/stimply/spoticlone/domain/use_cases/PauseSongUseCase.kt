package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.service.PlayerController

class PauseSongUseCase(
    private val controller: PlayerController
) {
    operator fun invoke() {
        controller.whenReady {
            controller.pause()
        }
    }
}