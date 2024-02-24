package com.stimply.spoticlone.data.mapper

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.stimply.spoticlone.domain.model.Song
import com.stimply.spoticlone.domain.model.SongDto
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

fun SongDto.toSong(requestManager: RequestManager): Song {
    var imageByteArray: ByteArray? = null

    val outputStream = ByteArrayOutputStream()

    val bitmap = requestManager
        .asBitmap()
        .load(imageUrl)
        .into(1000, 1000)
        .get()

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    imageByteArray = outputStream.toByteArray()

    return Song(
        imageByteArray = imageByteArray ?: byteArrayOf(),
        mediaId = mediaId,
        songUrl = songUrl,
        subtitle = subtitle,
        title = title
    )
}

fun Song.toMediaItem(): MediaItem {
    val mediaMetadata = MediaMetadata.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setArtist(subtitle)
        .setArtworkData(imageByteArray, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
        .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
        .build()

    return MediaItem.Builder()
        .setMediaId(mediaId)
        .setUri(songUrl)
        .setMediaMetadata(mediaMetadata)
        .build()
}

fun MediaItem.toSong(): Song {
    return Song(
        imageByteArray = mediaMetadata.artworkData ?: byteArrayOf(),
        mediaId = mediaId,
        songUrl = this.localConfiguration?.uri.toString(),
        subtitle = mediaMetadata.subtitle.toString(),
        title = mediaMetadata.title.toString()
    )
}