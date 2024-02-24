package com.stimply.spoticlone.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stimply.spoticlone.domain.model.SongDto
import com.stimply.spoticlone.domain.repository.SongRepository
import kotlinx.coroutines.tasks.await

class SongRepositoryImpl : SongRepository {
    override suspend fun getAllSongs(): List<SongDto> {
        return Firebase.firestore.collection("songs")
            .get()
            .await()
            .toObjects(SongDto::class.java)
    }
}