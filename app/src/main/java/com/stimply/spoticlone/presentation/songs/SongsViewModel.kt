package com.stimply.spoticlone.presentation.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.stimply.spoticlone.data.mapper.toSong
import com.stimply.spoticlone.domain.model.Song
import com.stimply.spoticlone.domain.service.PlayerControllerCallback
import com.stimply.spoticlone.domain.use_cases.SongUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val useCases: SongUseCases,
    private val requestManager: RequestManager
) : ViewModel(), PlayerControllerCallback {

    private val _songs = MutableStateFlow(listOf<Song>())
    val songs = _songs.asStateFlow()

    private val _uiState = MutableStateFlow(SongUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllSongs()
        useCases.addControllerCallbackUseCase(this)
    }

    override fun onCleared() {
        super.onCleared()
        useCases.removeControllerCallbackUseCase()
    }

    fun onEvent(event: SongsEvent) {
        when (event) {
            is SongsEvent.PlaySong -> {
                useCases.playSongUseCase(songIndexOf(event.song))
            }
            is SongsEvent.PauseSong -> {
                if (_uiState.value.isPlaying) {
                    useCases.pauseSongUseCase()
                } else {
                    useCases.playSongUseCase(null)
                }
            }
            is SongsEvent.UpdateMusicPosition -> {
                _uiState.value = _uiState.value.copy(
                    currentSongPosition = useCases.getMusicPositionUseCase()
                )
            }
            is SongsEvent.SetPlayerView -> {
                useCases.addPlayerToPlayerViewUseCase(event.playerView)
            }
            is SongsEvent.SetMusicPosition -> {
                useCases.seekToUseCase(event.position)
            }
            is SongsEvent.NextSong -> {
                useCases.nextSongUseCase()
            }
            is SongsEvent.PreviousSong -> {
                useCases.previousSongUseCase()
            }
        }
    }

    private fun songIndexOf(song: Song): Int {
        return _songs.value.indexOf(song)
    }

    private fun getAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val allSongs = useCases.getAllSongsUseCase()
            if (allSongs.isNotEmpty()) {
                val songsList = allSongs.map { songDto ->
                    songDto.toSong(requestManager)
                }
                _songs.emit(songsList)
                launch(Dispatchers.Main) {
                    useCases.addMediaItemsUseCase(_songs.value)
                }
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _uiState.value = _uiState.value.copy(
            isPlaying = isPlaying
        )
        println("isPlaying has changed: $isPlaying")
    }

    override fun onSongChanged(song: Song) {
        _uiState.value = _uiState.value.copy(
            currentlyPlayingSong = song
        )
    }

    override fun onDurationChanged(duration: Long) {
        _uiState.value = _uiState.value.copy(
            currentSongDuration = duration
        )
    }

    override fun onCurrentPositionChanged(position: Long) {
        _uiState.value = _uiState.value.copy(
            currentSongPosition = position
        )
    }
}