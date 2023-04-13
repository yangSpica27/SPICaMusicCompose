package me.spica.spicamusiccompose.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _selectIndex = mutableStateOf(0)

    val selectIndex: State<Int>
        get() = _selectIndex


    fun selectBottomNav(index: Int) {
        _selectIndex.value = index
    }

}