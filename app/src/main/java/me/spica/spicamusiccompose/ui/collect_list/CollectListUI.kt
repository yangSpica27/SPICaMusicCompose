package me.spica.spicamusiccompose.ui.collect_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import me.spica.spicamusiccompose.persistence.entity.Song

/**  收藏列表 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectListUI(collectListViewModel: CollectListViewModel = hiltViewModel()) {

    val songState = collectListViewModel.songs.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TitleBar()
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (songState.value == null) {
                LoadingPage()
            } else if (songState.value != null && songState.value!!.isEmpty()) {
                EmptyPage()
            } else {
                ListPage(songState.value ?: listOf())
            }
        }
    }
}


@Composable
private fun TitleBar() {

}

@Composable
private fun LoadingPage() {

}


@Composable
private fun ListPage(songList: List<Song>) {

}


@Composable
private fun EmptyPage() {

}