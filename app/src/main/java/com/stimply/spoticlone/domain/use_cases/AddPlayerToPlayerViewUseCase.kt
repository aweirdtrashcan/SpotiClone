package com.stimply.spoticlone.domain.use_cases

import androidx.media3.ui.PlayerView
import com.stimply.spoticlone.domain.service.PlayerController

class AddPlayerToPlayerViewUseCase(
    private val controller: PlayerController
) {
    operator fun invoke(playerView: PlayerView) {
        controller.addPlayerToPlayerView(playerView)
    }
}
