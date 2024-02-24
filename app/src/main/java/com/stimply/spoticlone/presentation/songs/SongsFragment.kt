package com.stimply.spoticlone.presentation.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stimply.spoticlone.R
import com.stimply.spoticlone.databinding.FragmentSongsBinding
import com.stimply.spoticlone.domain.model.Song
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SongsFragment : Fragment() {

    private lateinit var binding: FragmentSongsBinding
    private val viewModel by activityViewModels<SongsViewModel>()
    private val adapter by lazy {
        SongsAdapter { onSongClicked(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSongsAdapter()
        listenToAllSongs()
        listenForUiState()
        listenForControlsButton()
    }

    private fun listenForControlsButton() {
        val cardView = binding.currentlyPlayingSongCardView
        cardView.setPausePlayButtonClickListener {
            viewModel.onEvent(SongsEvent.PauseSong)
        }
        cardView.setOnCardClickListener {
            findNavController().navigate(R.id.fromSongFragmentToSongInfoFragment)
        }
    }

    private fun listenForUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.currentlyPlayingSongCardView.setCurrentlyPlayingSong(it.currentlyPlayingSong)
                    binding.currentlyPlayingSongCardView.setIsSongPlaying(it.isPlaying)
                }
            }
        }
    }

    private fun setupSongsAdapter() {
        binding.songsRecyclerView.adapter = adapter
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun listenToAllSongs() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.songs.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun onSongClicked(song: Song) {
        viewModel.onEvent(SongsEvent.PlaySong(song))
    }

}