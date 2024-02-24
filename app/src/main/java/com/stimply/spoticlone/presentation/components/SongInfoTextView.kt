package com.stimply.spoticlone.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.stimply.spoticlone.R
import com.stimply.spoticlone.databinding.ResTextViewSongInfoBinding

class SongInfoTextView(
    context: Context,
    private val attrs: AttributeSet
): LinearLayout(context, attrs) {

    private val binding: ResTextViewSongInfoBinding =
        ResTextViewSongInfoBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        getAttributes()
    }

    private fun getAttributes() {
        val styledAttrs = context.obtainStyledAttributes(
            attrs,
            R.styleable.SongInfoTextView
        )

        val title = styledAttrs.getString(R.styleable.SongInfoTextView_title)
        val artist = styledAttrs.getString(R.styleable.SongInfoTextView_artist)

        setTitle(title)
        setArtist(artist)

        styledAttrs.recycle()
    }

    fun setTitle(title: String?) {
        binding.title.text = title
    }

    fun setArtist(artist: String?) {
        binding.artist.text = artist
    }

    fun getTitle(): CharSequence {
        return binding.title.text
    }

    fun getArtist(): CharSequence {
        return binding.artist.text
    }

}