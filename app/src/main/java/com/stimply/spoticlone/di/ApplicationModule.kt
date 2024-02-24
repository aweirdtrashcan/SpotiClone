package com.stimply.spoticlone.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.stimply.spoticlone.data.repository.SongRepositoryImpl
import com.stimply.spoticlone.data.service.DefaultPlayerController
import com.stimply.spoticlone.domain.repository.SongRepository
import com.stimply.spoticlone.domain.service.PlayerController
import com.stimply.spoticlone.domain.use_cases.AddControllerCallbackUseCase
import com.stimply.spoticlone.domain.use_cases.AddMediaItemsUseCase
import com.stimply.spoticlone.domain.use_cases.AddPlayerToPlayerViewUseCase
import com.stimply.spoticlone.domain.use_cases.GetAllSongsUseCase
import com.stimply.spoticlone.domain.use_cases.GetMusicPositionUseCase
import com.stimply.spoticlone.domain.use_cases.NextSongUseCase
import com.stimply.spoticlone.domain.use_cases.PauseSongUseCase
import com.stimply.spoticlone.domain.use_cases.PlaySongUseCase
import com.stimply.spoticlone.domain.use_cases.PreviousSongUseCase
import com.stimply.spoticlone.domain.use_cases.RemoveControllerCallbackUseCase
import com.stimply.spoticlone.domain.use_cases.SeekToUseCase
import com.stimply.spoticlone.domain.use_cases.SongUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideSongRepository(): SongRepository {
        return SongRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGlideRequestManager(
        @ApplicationContext context: Context
    ): RequestManager {
        return Glide.with(context)
    }

    @Provides
    @Singleton
    fun provideSongUseCases(
        repository: SongRepository,
        playerController: PlayerController
    ): SongUseCases {
        return SongUseCases(
            GetAllSongsUseCase(repository),
            PlaySongUseCase(playerController),
            PauseSongUseCase(playerController),
            AddMediaItemsUseCase(playerController),
            AddControllerCallbackUseCase(playerController),
            RemoveControllerCallbackUseCase(playerController),
            GetMusicPositionUseCase(playerController),
            AddPlayerToPlayerViewUseCase(playerController),
            SeekToUseCase(playerController),
            NextSongUseCase(playerController),
            PreviousSongUseCase(playerController)
        )
    }

    @Provides
    @Singleton
    fun providePlayerController(
        @ApplicationContext context: Context
    ): PlayerController {
        return DefaultPlayerController(context)
    }
}