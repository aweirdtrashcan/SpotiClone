package com.stimply.spoticlone.domain.model

data class SongDto(
    val imageUrl: String = "",
    val mediaId: String = "",
    val songUrl: String = "",
    val subtitle: String = "",
    val title: String = ""
)