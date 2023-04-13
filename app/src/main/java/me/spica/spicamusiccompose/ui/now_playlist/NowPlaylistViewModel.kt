package me.spica.spicamusiccompose.ui.now_playlist

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlaylistViewModel @Inject constructor() :ViewModel() {

    val listState = LazyListState()

}