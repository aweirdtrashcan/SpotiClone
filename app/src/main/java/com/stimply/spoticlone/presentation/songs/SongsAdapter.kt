package com.stimply.spoticlone.presentation.songs

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.stimply.spoticlone.databinding.ResSongItemBinding
import com.stimply.spoticlone.domain.model.Song
import com.stimply.spoticlone.domain.model.SongDto
import java.io.ByteArrayInputStream
import java.util.BitSet

class SongsAdapter(
    private val onClick: (Song) -> Unit
) : ListAdapter<Song, SongsAdapter.SongViewHolder>(SongDiffUtil())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ResSongItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    inner class SongViewHolder(
        private val binding: ResSongItemBinding
    ): ViewHolder(binding.root) {
        fun bind(item: Song, onClick: (Song) -> Unit) {
            val inputStream = ByteArrayInputStream(item.imageByteArray)
            binding.tvSongTitle.text = item.title
            binding.tvSongArtist.text = item.subtitle
            binding.ivSongIcon.setImageBitmap(BitmapFactory.decodeStream(inputStream))
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    class SongDiffUtil: DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

    }
}