package com.stimply.spoticlone.presentation.components

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.stimply.spoticlone.R
import com.stimply.spoticlone.databinding.ResCardViewPlayingSongBinding
import com.stimply.spoticlone.domain.model.Song

class CardViewPlayingSong(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs) {

    private val binding = ResCardViewPlayingSongBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var currentlyPlayingSong: Song? = null

    init {
        binding.root.visibility = View.GONE
    }

    fun setCurrentlyPlayingSong(song: Song?) {
        if (song == null) return
        currentlyPlayingSong = song
        setCurrentlyPlayingSongMetadata()
    }

    private fun setCurrentlyPlayingSongMetadata() {
        currentlyPlayingSong?.let { song: Song ->
            binding.cardIcon.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    song.imageByteArray,
                    0,
                    song.imageByteArray.size
                )
            )
            binding.songInfoTextView.setTitle(song.title)
            binding.songInfoTextView.setArtist(song.subtitle)
        }
    }

    fun setIsSongPlaying(isPlaying: Boolean) {
        if (isPlaying && binding.root.visibility == View.GONE) {
            binding.root.visibility = View.VISIBLE
        }
        setPauseButtonIcon(isPlaying)
    }

    private fun setPauseButtonIcon(isPlaying: Boolean) {
        if (isPlaying) {
            binding.btnPauseSong.background = AppCompatResources.getDrawable(
                context,
                R.drawable.ic_exo_icon_pause
            )
        } else {
            binding.btnPauseSong.background = AppCompatResources.getDrawable(
                context,
                R.drawable.ic_exo_icon_play
            )
        }
    }

    fun setPausePlayButtonClickListener(onClick: (View) -> Unit) {
        binding.btnPauseSong.setOnClickListener(onClick)
    }

    fun setOnCardClickListener(onClick: (Song?) -> Unit) {
        binding.root.setOnClickListener {
            onClick(currentlyPlayingSong)
        }
    }

}