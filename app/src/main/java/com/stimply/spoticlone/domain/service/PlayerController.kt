package com.stimply.spoticlone.domain.service

import androidx.media3.ui.PlayerView
import com.stimply.spoticlone.domain.model.Song

typealias WhenReadyCallback = () -> Unit

interface PlayerController {

    fun addMediaItems(songs: List<Song>)
    fun prepare()
    fun play()
    fun play(songIndex: Int)
    fun seekTo(posMs: Long)
    fun pause()
    fun setCallback(callback: PlayerControllerCallback)
    fun removeCallback()
    fun whenReady(whenReady: WhenReadyCallback)
    fun getCurrentPosition(): Long
    fun addPlayerToPlayerView(playerView: PlayerView)
    fun next()
    fun previous()
    fun onDestroy()
}