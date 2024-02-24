package com.stimply.spoticlone.data.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.MoreExecutors
import com.stimply.spoticlone.data.mapper.toMediaItem
import com.stimply.spoticlone.data.mapper.toSong
import com.stimply.spoticlone.domain.model.Song
import com.stimply.spoticlone.domain.service.PlayerController
import com.stimply.spoticlone.domain.service.PlayerControllerCallback
import com.stimply.spoticlone.domain.service.WhenReadyCallback

class DefaultPlayerController(
    private val context: Context
): PlayerController {

    private lateinit var mediaController: MediaController
    private lateinit var playerListener: Player.Listener
    private val callbacks = mutableListOf<WhenReadyCallback>()
    private var callback: PlayerControllerCallback? = null

    private var isReady = false
        set(value) {
            field = value
            if (value) {
                callbacks.forEach { it() }
                callbacks.removeAll(callbacks)
            }
        }

    init {
        createController()
    }

    private fun createController() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, PlayerService::class.java)
        )
        val builder = MediaController.Builder(context, sessionToken)
            .buildAsync()

        builder.addListener({ controllerListener(builder.get()) }, MoreExecutors.directExecutor())
    }

    private fun controllerListener(mediaController: MediaController) {
        this.mediaController = mediaController

        playerListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                callback?.let { cb ->
                    val song = player.currentMediaItem
                    song?.let {
                        cb.onSongChanged(it.toSong())
                    }
                    cb.onIsPlayingChanged(player.isPlaying)
                    if (player.isPlaying) {
                        cb.onDurationChanged(player.duration)
                        cb.onCurrentPositionChanged(player.currentPosition)
                    }
                }
            }
        }

        this.mediaController.addListener(playerListener)
        isReady = true
    }

    override fun setCallback(callback: PlayerControllerCallback) {
        this.callback = callback
    }

    override fun removeCallback() {
        this.callback = null
    }

    override fun addMediaItems(songs: List<Song>) {
        mediaController.setMediaItems(songs.map { it.toMediaItem() })
    }

    override fun prepare() {
        mediaController.prepare()
    }

    override fun play() {
        mediaController.play()
    }

    override fun play(songIndex: Int) {
        mediaController.seekToDefaultPosition(songIndex)
        mediaController.playWhenReady = true
        mediaController.prepare()
    }

    override fun seekTo(posMs: Long) {
        mediaController.seekTo(posMs)
    }

    override fun pause() {
        mediaController.pause()
    }

    override fun whenReady(whenReady: WhenReadyCallback) {
        if (!isReady) {
            callbacks.add(whenReady)
        } else {
            whenReady()
        }
    }

    override fun getCurrentPosition(): Long {
        return mediaController.currentPosition
    }

    override fun addPlayerToPlayerView(playerView: PlayerView) {
        playerView.player = mediaController
    }

    override fun next() {
        mediaController.seekToNext()
    }

    override fun previous() {
        mediaController.seekToPrevious()
    }

    override fun onDestroy() {
        if (!this::mediaController.isInitialized) return
        if (mediaController.isPlaying) {
            mediaController.stop()
        }
        mediaController.removeListener(playerListener)
        mediaController.release()
    }
}