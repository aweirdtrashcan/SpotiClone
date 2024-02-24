package com.stimply.spoticlone.domain.repository

import com.stimply.spoticlone.domain.model.SongDto

interface SongRepository {
    suspend fun getAllSongs(): List<SongDto>
}