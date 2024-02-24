package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.model.Song
import com.stimply.spoticlone.domain.service.PlayerController

class AddMediaItemsUseCase(
    private val controller: PlayerController
) {
    operator fun invoke(mediaItems: List<Song>) {
        controller.whenReady {
            controller.addMediaItems(mediaItems)
        }
    }
}