package me.spica.spicamusiccompose.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import me.spica.spicamusiccompose.service.MusicService
import me.spica.spicamusiccompose.ui.theme.AppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMusicService()
        setContent {
            AppTheme {
                MainUI()
            }
        }
    }

    private fun initMusicService() {
        startService(Intent(this, MusicService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
    }
}

