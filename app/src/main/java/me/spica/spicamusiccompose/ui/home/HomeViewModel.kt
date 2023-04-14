package me.spica.spicamusiccompose.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {


    private val _pagerState = PagerState(initialPage = 0, initialPageOffsetFraction = 0f)

    val pagerState: PagerState
        get() = _pagerState


   suspend fun selectBottomNav(index: Int) {
        _pagerState.animateScrollToPage(index)
    }

}