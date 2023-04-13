package me.spica.spicamusiccompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.persistence.dao.SongDao
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.playback.PlaybackStateManager
import me.spica.spicamusiccompose.playback.RepeatMode
import me.spica.spicamusiccompose.player.Queue
import javax.inject.Inject

@HiltViewModel
class PlayStateViewModel @Inject constructor(
    private val songDao: SongDao
) : ViewModel(), PlaybackStateManager.Listener {
    // 播放列表
    private val _songsList = MutableStateFlow(listOf<Song>())
    val songsList: Flow<List<Song>>
        get() = _songsList


    // 当前播放的乐曲
    private val _song = MutableStateFlow<Song?>(null)


    val currentSongFlow: Flow<Song>
        get() = _song.filterNotNull()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)


    @OptIn(ExperimentalCoroutinesApi::class)
    val likeFlow: Flow<Boolean>
        get() = _song.filterNotNull()
            .flatMapLatest {
                songDao.getSongIsLikeFlowWithId(it.songId ?: 0)
            }
            .map { it == 1 }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)


    // 是否正在播放
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: Flow<Boolean>
        get() = _isPlaying

    // 当前的进度
    private val _positionDs = flow {
        while (true) {
            PlaybackStateManager.getInstance().synchronizeState()
            emit(PlaybackStateManager.getInstance().playerState.currentPositionMs)
            delay(1000)
        }
    }
    val positionDs: Flow<Long>
        get() = _positionDs.distinctUntilChanged()


    // 循环模式
    private val _repeatMode = MutableStateFlow(RepeatMode.NONE)
    val repeatMode: Flow<RepeatMode>
        get() = _repeatMode


    // 是否随机播放
    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: Flow<Boolean>
        get() = _isShuffled


    init {
        PlaybackStateManager.getInstance().addListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        PlaybackStateManager.getInstance().removeListener(this)
    }


    fun togglePlaying() {
        PlaybackStateManager.getInstance().setPlaying(!PlaybackStateManager.getInstance().playerState.isPlaying)
    }

    fun playPre() {
        PlaybackStateManager.getInstance().playPre()
    }

    fun playNext() {
        PlaybackStateManager.getInstance().playNext()
    }

    fun play(song: Song, list: List<Song>) {
        viewModelScope.launch {
            PlaybackStateManager.getInstance().playAsync(song, list)
        }

    }

    fun play(song: Song) {
        viewModelScope.launch {
            PlaybackStateManager.getInstance().playAsync(song)
        }
    }

    fun seekTo(position: Long) {
        PlaybackStateManager.getInstance().seekTo(position)
    }


    override fun onIndexMoved(queue: Queue) {
        super.onIndexMoved(queue)
        viewModelScope.launch {
            _song.emit(queue.currentSong())
        }
    }

    override fun onNewListLoad(queue: Queue) {
        super.onNewListLoad(queue)
        viewModelScope.launch {
            _songsList.emit(queue.heap)
            _song.emit(queue.currentSong())
        }

    }

    override fun onStateChanged(isPlaying: Boolean) {
        super.onStateChanged(isPlaying)
        _isPlaying.value = isPlaying
    }

    override fun onRepeatChanged(repeatMode: RepeatMode) {
        super.onRepeatChanged(repeatMode)
        _repeatMode.value = repeatMode
    }
}