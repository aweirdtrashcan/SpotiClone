package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.service.PlayerController
import com.stimply.spoticlone.domain.service.PlayerControllerCallback

class AddControllerCallbackUseCase(
    private val controller: PlayerController
) {
    operator fun invoke(controllerCallback: PlayerControllerCallback) {
        controller.setCallback(controllerCallback)
    }
}