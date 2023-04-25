package me.spica.spicamusiccompose.ui.collect_list

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.persistence.dao.SongDao
import me.spica.spicamusiccompose.persistence.entity.Song
import javax.inject.Inject

@HiltViewModel
class CollectListViewModel @Inject constructor(private val songDao: SongDao) : ViewModel() {


    private val _songs = MutableStateFlow<List<Song>>(listOf())

    val songs: Flow<List<Song>> = _songs

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _songs.emit(songDao.getAllSync())
        }
    }

    fun search(keyWords: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!TextUtils.isEmpty(keyWords)) {
                _songs.emit(songDao.getCollectSongsFromNameSync(keyWords))
            } else {
                _songs.emit(songDao.getAllSync())
            }

        }
    }

}