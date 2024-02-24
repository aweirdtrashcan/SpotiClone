package com.stimply.spoticlone.domain.service

import com.stimply.spoticlone.domain.model.Song

interface PlayerControllerCallback {

    fun onIsPlayingChanged(isPlaying: Boolean)
    fun onSongChanged(song: Song)
    fun onDurationChanged(duration: Long)
    fun onCurrentPositionChanged(position: Long)
}