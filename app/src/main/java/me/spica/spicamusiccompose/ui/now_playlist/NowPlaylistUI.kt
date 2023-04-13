package me.spica.spicamusiccompose.ui.now_playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.ui.common.ViewModelProvider
import me.spica.spicamusiccompose.ui.viewmodel.LocalMusicViewModel
import me.spica.spicamusiccompose.ui.viewmodel.PlayStateViewModel


@Composable
fun NowPlayListUI(
    localMusicViewModel: LocalMusicViewModel = ViewModelProvider.localMusic,
    nowPlaylistViewModel: NowPlaylistViewModel
) {
    val mediaList = localMusicViewModel.songs.collectAsState(initial = listOf())
    Column {
        TitleBar()
        Divider(modifier = Modifier.fillMaxWidth(), thickness = .5.dp)
        LazyColumn(
            state = nowPlaylistViewModel.listState,
            content = {
                itemsIndexed(mediaList.value) { _, item ->
                    MediaItem(song = item)
                }
            })
    }
}


@Composable
private fun MediaItem(song: Song, playStateViewModel: PlayStateViewModel = ViewModelProvider.playState) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .clickable {

            }
            .padding(horizontal = 22.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = song.displayName, style = MaterialTheme.typography.subtitle1)
            Text(text = song.artist, style = MaterialTheme.typography.subtitle2, modifier = Modifier.alpha(.5f))
        }
        IconButton(onClick = { }, modifier = Modifier.width(24.dp)) {
            AsyncImage(model = R.drawable.ic_delete, contentDescription = null)
        }
    }
}


@Composable
fun TitleBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(horizontal = 22.dp),
        modifier = Modifier.statusBarsPadding(),
        elevation = 0.dp
    ) {
        Text(text = stringResource(R.string.now_play_list), color = MaterialTheme.colors.onSurface)
    }
}
