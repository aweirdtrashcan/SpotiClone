package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.service.PlayerController

class GetMusicPositionUseCase(
    private val controller: PlayerController
) {
    operator fun invoke(): Long {
        return controller.getCurrentPosition()
    }
}