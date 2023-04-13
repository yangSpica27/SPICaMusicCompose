package me.spica.spicamusiccompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.persistence.dao.SongDao
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.playback.PlaybackStateManager
import javax.inject.Inject


@HiltViewModel
class LocalMusicViewModel
@Inject constructor(
    private val songDao: SongDao
) : ViewModel() {

    val songs = songDao.getAll().distinctUntilChanged().flowOn(Dispatchers.IO)

    val likeSongs = songDao.getAllLikeSong().distinctUntilChanged().flowOn(Dispatchers.IO)

    fun toggleLike(song: Song? = PlaybackStateManager.getInstance().getCurrentSong()) {
        viewModelScope.launch {
            songDao.toggleLike(song?.songId ?: 0)
        }
    }

}