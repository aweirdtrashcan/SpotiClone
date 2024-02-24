package com.stimply.spoticlone.presentation.song_info

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Transformation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.stimply.spoticlone.R
import com.stimply.spoticlone.databinding.FragmentSongInfoBinding
import com.stimply.spoticlone.domain.model.Song
import com.stimply.spoticlone.domain.repository.SongRepository
import com.stimply.spoticlone.presentation.songs.SongUiState
import com.stimply.spoticlone.presentation.songs.SongsEvent
import com.stimply.spoticlone.presentation.songs.SongsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@AndroidEntryPoint
class SongInfoFragment : Fragment() {

    private lateinit var binding: FragmentSongInfoBinding

    private val viewModel by activityViewModels<SongsViewModel>()

    private var timer: Timer? = null

    private var lastPlayingSong: Song? = null
    private var wasPlaying = false

    @Inject
    lateinit var repository: SongRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenForUiState()
        updateSongDuration()
        listenForControlButtons()
    }

    private fun listenForControlButtons() {
        binding.btnNext.setOnClickListener {
            viewModel.onEvent(SongsEvent.NextSong)
        }
        binding.btnPause.setOnClickListener {
            viewModel.onEvent(SongsEvent.PauseSong)
        }
        binding.btnPrevious.setOnClickListener {
            viewModel.onEvent(SongsEvent.PreviousSong)
        }
    }

    private fun listenForUiState() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    setCurrentlyPlayingSong(it)
                    setCorrectPausePlayButtonIcon(it)
                }
            }
        }
    }

    private fun setCorrectPausePlayButtonIcon(uiState: SongUiState) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (wasPlaying != uiState.isPlaying) {
                println("setCorrectPausePlayButtonIcon: ${uiState.isPlaying}")
                if (uiState.isPlaying) {
                    binding.btnPause.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_exo_icon_pause
                        )
                    )
                } else {
                    binding.btnPause.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_exo_icon_play
                        )
                    )
                }
                wasPlaying = uiState.isPlaying
            }
        }
    }

    private fun setCurrentlyPlayingSong(
        it: SongUiState
    ) {
        if (it.currentlyPlayingSong != lastPlayingSong) {
            lastPlayingSong = it.currentlyPlayingSong
            lifecycleScope.launch(Dispatchers.Main) {
                Glide.with(requireContext())
                    .load(it.currentlyPlayingSong?.imageByteArray)
                    .into(binding.ivAlbumIcon)

                binding.tvSongTitleInfo.text = it.currentlyPlayingSong?.title
                binding.tvSongArtist.text = it.currentlyPlayingSong?.subtitle
                setBackgroundColor(it.currentlyPlayingSong?.imageByteArray)
            }
        }
    }

    private fun setBackgroundColor(imageByteArray: ByteArray?) {
        imageByteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            binding.ivBackgroundImage.setImageBitmap(bitmap)
            binding.ivBackgroundImage.setRenderEffect(
                RenderEffect.createBlurEffect(90.0f, 90.0f, Shader.TileMode.CLAMP)
            )
        }
    }

    private fun updateSongDuration() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                lifecycleScope.launch(Dispatchers.Main) {
                    if (viewModel.uiState.value.isPlaying) {
                        var songPos: String
                        (viewModel.uiState.value.currentSongPosition / 1000).toDuration(
                            DurationUnit.SECONDS
                        ).toComponents { minutes, seconds, _ ->
                            songPos = String.format(
                                "%s:%s",
                                convertToTwoDigits(minutes.toInt()),
                                convertToTwoDigits(seconds)
                            )
                        }

                        var songDuration: String
                        (viewModel.uiState.value.currentSongDuration / 1000).toDuration(
                            DurationUnit.SECONDS
                        ).toComponents { minutes, seconds, _ ->
                            songDuration = String.format(
                                "%s:%s",
                                convertToTwoDigits(minutes.toInt()),
                                convertToTwoDigits(seconds)
                            )
                        }

                        viewModel.onEvent(SongsEvent.UpdateMusicPosition)
                        binding.tvSongPosition.text = songPos
                        binding.tvSongDuration.text = songDuration

                        binding.slider.valueFrom = 0.0f
                        binding.slider.valueTo =
                            viewModel.uiState.value.currentSongDuration.toFloat()
                        binding.slider.value = viewModel.uiState.value.currentSongPosition.toFloat()
                    }
                }
            }

        }, 0, 500)
        binding.slider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.onEvent(SongsEvent.SetMusicPosition(value.toLong()))
            }
        }
    }

    private fun convertToTwoDigits(number: Int): String {
        return if (number == 0) {
            "00"
        } else if (number in 1..9) {
            "0$number"
        } else {
            "$number"
        }
    }
}