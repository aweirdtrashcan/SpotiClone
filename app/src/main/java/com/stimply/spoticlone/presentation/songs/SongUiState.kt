package com.stimply.spoticlone.presentation.songs

import com.stimply.spoticlone.domain.model.Song

data class SongUiState(
    val currentlyPlayingSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentSongDuration: Long = 0L,
    val currentSongPosition: Long = 0L
)