package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.service.PlayerController

class PlaySongUseCase(
    private val controller: PlayerController
) {

    operator fun invoke(songIndex: Int?) {
        controller.whenReady {
            if (songIndex != null) {
                controller.play(songIndex)
            } else {
                controller.play()
            }
        }
    }
}