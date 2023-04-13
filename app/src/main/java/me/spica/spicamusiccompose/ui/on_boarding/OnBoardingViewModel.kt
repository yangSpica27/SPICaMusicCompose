package me.spica.spicamusiccompose.ui.on_boarding

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.audio.scanner.FlowEvent
import me.spica.spicamusiccompose.audio.scanner.MediaStoreMediaProvider
import me.spica.spicamusiccompose.audio.scanner.MessageProgress
import me.spica.spicamusiccompose.persistence.dao.SongDao
import me.spica.spicamusiccompose.ui.navgation.Navigator
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val mediaStoreMediaProvider: MediaStoreMediaProvider,
    private val songDao: SongDao
) : ViewModel() {

    private val _scannerProgressState: MutableState<MessageProgress> = mutableStateOf(MessageProgress("准备中", null))
    val scannerProgressState: State<MessageProgress>
        get() = _scannerProgressState

    private val _isScanner: MutableState<Boolean> = mutableStateOf(false)
    val isScanner: State<Boolean> get() = _isScanner

    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean>
        get() = _isSuccess

    fun scannerSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaStoreMediaProvider.findSongs().collect { event ->
                when (event) {
                    is FlowEvent.Failure -> {

                    }
                    is FlowEvent.Progress -> {
                        _isScanner.value = true
                        _scannerProgressState.value = event.data
                    }
                    is FlowEvent.Success -> {
                        songDao.deleteAllSync()
                        songDao.insertSync(event.result)
                        _isSuccess.value = true
                    }
                }
            }
        }

    }

}