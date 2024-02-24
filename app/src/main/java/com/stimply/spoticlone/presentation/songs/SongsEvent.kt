package com.stimply.spoticlone.presentation.songs

import androidx.media3.ui.PlayerView
import com.stimply.spoticlone.domain.model.Song

sealed class SongsEvent {
    data class PlaySong(val song: Song): SongsEvent()
    data object PauseSong: SongsEvent()
    data object NextSong: SongsEvent()
    data object PreviousSong: SongsEvent()
    data object UpdateMusicPosition: SongsEvent()
    data class SetMusicPosition(val position: Long): SongsEvent()
    data class SetPlayerView(val playerView: PlayerView): SongsEvent()
}