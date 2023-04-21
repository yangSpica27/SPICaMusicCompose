package me.spica.spicamusiccompose.ui.local_music

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.ui.viewmodel.LocalMusicViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalMusicUI(localMusicViewModel: LocalMusicViewModel = hiltViewModel()) {
    val songsState = localMusicViewModel.songs.collectAsState(initial = null)
    val listState = rememberLazyListState()
    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            TitleBar()
            Divider(thickness = .5.dp)
            Box(modifier = Modifier.weight(1f)) {
                if (songsState.value == null) {
                    LoadingPage()
                } else if (songsState.value.isNullOrEmpty()) {
                    EmptyPage()
                } else {
                    ListPage(
                        songsState = songsState,
                        lazyListState = listState,
                        localMusicViewModel = localMusicViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingPage() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.is_loading), style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun ListPage(
    songsState: State<List<Song>?>,
    lazyListState: LazyListState,
    localMusicViewModel: LocalMusicViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = lazyListState
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(songsState.value ?: listOf()) {
            Column {
                SongItem(song = it, localMusicViewModel = localMusicViewModel)
            }
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Text(
                text = stringResource(R.string.no_more_data),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
private fun SongItem(song: Song, localMusicViewModel: LocalMusicViewModel) {
    Row(
        modifier = Modifier
            .clickable {

            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(22.dp))
        AsyncImage(
            model = song.getCoverUri(),
            contentDescription = null,
            modifier = Modifier
                .width(54.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.displayName, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = song.artist, style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(.5f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = {
                localMusicViewModel.toggleLike(song = song)
            }, modifier = Modifier
                .width(34.dp)
                .height(34.dp)
                .clip(CircleShape)
                .padding(4.dp)
        ) {
            AsyncImage(
                model = R.drawable.ic_like,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (song.like) {
                        Color.Red
                    } else {
                        Color.LightGray
                    }
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = { }, modifier = Modifier
                .width(34.dp)
                .height(34.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(4.dp)
        ) {
            AsyncImage(model = R.drawable.ic_more, contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer))
        }
        Spacer(modifier = Modifier.width(22.dp))
    }
}

@Composable
private fun EmptyPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = R.drawable.plates_select_re_3kbd, contentDescription = null)
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = { }) {
            Text(text = stringResource(id = R.string.no_data))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleBar() {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(
                text = stringResource(R.string.local_music),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}