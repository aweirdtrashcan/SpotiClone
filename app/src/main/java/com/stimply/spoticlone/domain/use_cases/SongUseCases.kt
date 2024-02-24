package com.stimply.spoticlone.domain.use_cases

data class SongUseCases(
    val getAllSongsUseCase: GetAllSongsUseCase,
    val playSongUseCase: PlaySongUseCase,
    val pauseSongUseCase: PauseSongUseCase,
    val addMediaItemsUseCase: AddMediaItemsUseCase,
    val addControllerCallbackUseCase: AddControllerCallbackUseCase,
    val removeControllerCallbackUseCase: RemoveControllerCallbackUseCase,
    val getMusicPositionUseCase: GetMusicPositionUseCase,
    val addPlayerToPlayerViewUseCase: AddPlayerToPlayerViewUseCase,
    val seekToUseCase: SeekToUseCase,
    val nextSongUseCase: NextSongUseCase,
    val previousSongUseCase: PreviousSongUseCase
)