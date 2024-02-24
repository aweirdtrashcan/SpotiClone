package com.stimply.spoticlone.presentation

import android.Manifest
import android.content.ComponentName
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.stimply.spoticlone.databinding.ActivityMainBinding
import com.stimply.spoticlone.data.service.PlayerService
import com.stimply.spoticlone.domain.service.PlayerController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var playerController: PlayerController

    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaController: MediaController
    private val callbacks = arrayListOf<(MediaController) -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    override fun onStart() {
        super.onStart()

        val componentName = ComponentName(this, PlayerService::class.java)
        val sessionToken = SessionToken(this, componentName)
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        val runnable = Runnable {
            mediaController = controllerFuture.get()
            for (c in callbacks) {
                c(mediaController)
            }
        }
        controllerFuture.addListener(runnable, MoreExecutors.directExecutor())
    }

    override fun onDestroy() {
        super.onDestroy()
        playerController.onDestroy()
    }

    private fun requestPermissions() {
        val permissions = listOf(
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK,
            Manifest.permission.POST_NOTIFICATIONS
        )
        requestPermissions(permissions.toTypedArray(), 3)
    }
}