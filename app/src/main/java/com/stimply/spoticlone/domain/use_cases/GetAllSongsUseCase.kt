package com.stimply.spoticlone.domain.use_cases

import com.stimply.spoticlone.domain.model.SongDto
import com.stimply.spoticlone.domain.repository.SongRepository
import javax.inject.Inject

class GetAllSongsUseCase @Inject constructor(
    private val repository: SongRepository
) {

    suspend operator fun invoke(): List<SongDto> {
        return repository.getAllSongs()
    }

}