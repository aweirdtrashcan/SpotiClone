package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.service.PlayerController
import com.stimply.spoticlone.domain.service.PlayerControllerCallback

class RemoveControllerCallbackUseCase(
    private val controller: PlayerController
) {
    operator fun invoke() {
        controller.removeCallback()
    }
}