package com.stimply.spoticlone.data.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.DefaultMediaNotificationProvider.DEFAULT_CHANNEL_ID
import androidx.media3.session.DefaultMediaNotificationProvider.DEFAULT_CHANNEL_NAME_RESOURCE_ID
import androidx.media3.session.DefaultMediaNotificationProvider.DEFAULT_NOTIFICATION_ID
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.stimply.spoticlone.domain.util.SongsConstants.ACTION_PLAYER_STATE_BUFFERING
import com.stimply.spoticlone.domain.util.SongsConstants.ACTION_PLAYER_STATE_ENDED
import com.stimply.spoticlone.domain.util.SongsConstants.ACTION_PLAYER_STATE_IDLE
import com.stimply.spoticlone.domain.util.SongsConstants.ACTION_PLAYER_STATE_READY
import com.stimply.spoticlone.domain.util.SongsConstants.NOTIFICATION_CLICK_REQUEST_ID
import com.stimply.spoticlone.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerService : MediaSessionService(), Player.Listener, MediaSession.Callback {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private val mediaItems = mutableListOf<MediaItem>()
    private val notificationId = 1

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    @OptIn(UnstableApi::class) private fun startup() {
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder().setContentType(
                    C.AUDIO_CONTENT_TYPE_MUSIC
                ).build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .build()

        player.addListener(this)

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(this)
            .build()

        setMediaNotificationProvider(object : MediaNotification.Provider {
            override fun createNotification(
                mediaSession: MediaSession,
                customLayout: ImmutableList<CommandButton>,
                actionFactory: MediaNotification.ActionFactory,
                onNotificationChangedCallback: MediaNotification.Provider.Callback
            ): MediaNotification {
                return createMediaNotification(
                    mediaSession,
                    customLayout,
                    actionFactory,
                    onNotificationChangedCallback
                )
            }

            override fun handleCustomCommand(
                session: MediaSession,
                action: String,
                extras: Bundle
            ): Boolean {
                return true
            }

        })
    }

    @OptIn(UnstableApi::class) private fun createMediaNotification(
        mediaSession: MediaSession,
        customLayout: ImmutableList<CommandButton>,
        actionFactory: MediaNotification.ActionFactory,
        onNotificationChangedCallback: MediaNotification.Provider.Callback
    ): MediaNotification {
        val notBuilder = DefaultMediaNotificationProvider.Builder(this@PlayerService)
            .setNotificationId(DEFAULT_NOTIFICATION_ID)
            .setChannelId(DEFAULT_CHANNEL_ID)
            .setChannelName(DEFAULT_CHANNEL_NAME_RESOURCE_ID)
            .build()

        val notification = notBuilder.createNotification(
            mediaSession,
            customLayout,
            actionFactory,
            onNotificationChangedCallback
        )

        val intent = Intent(this@PlayerService, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this@PlayerService,
            NOTIFICATION_CLICK_REQUEST_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notification.notification.contentIntent = pendingIntent

        return notification
    }

    override fun onCreate() {
        super.onCreate()

        startup()
    }

    override fun onDestroy() {
        super.onDestroy()

        player.release()
        mediaSession.release()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        val action: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> {
                ACTION_PLAYER_STATE_IDLE
            }

            ExoPlayer.STATE_READY -> {
                ACTION_PLAYER_STATE_READY
            }

            ExoPlayer.STATE_BUFFERING -> {
                ACTION_PLAYER_STATE_BUFFERING
            }

            ExoPlayer.STATE_ENDED -> {
                ACTION_PLAYER_STATE_ENDED
            }

            else -> ""
        }

        applicationContext.sendBroadcast(Intent(action))
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        this.mediaItems += mediaItems
        return super.onAddMediaItems(mediaSession, controller, mediaItems)
    }

/*    @OptIn(UnstableApi::class)
    private fun createMediaNotification(): MediaNotification {
        val intent = Intent(this, SongInfoFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_CLICK_REQUEST_ID,
            intent,
            FLAG_UPDATE_CURRENT or FLAG_MUTABLE
        )

        val channelId = getString(R.string.notification_channel_id)

        val songName = mediaSession.player.currentMediaItem?.mediaMetadata?.title
        val artistName = mediaSession.player.currentMediaItem?.mediaMetadata?.subtitle

    }*/

    @UnstableApi
    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        return Futures.immediateFuture(
            MediaSession.MediaItemsWithStartPosition(
                mediaItems,
                0,
                0L
            )
        )
    }
}