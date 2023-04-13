package me.spica.spicamusiccompose.ui.now_playlist

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.ui.common.ViewModelProvider
import me.spica.spicamusiccompose.ui.viewmodel.PlayStateViewModel


@Composable
fun NowPlayListUI(
    nowPlaylistViewModel: NowPlaylistViewModel,
    playStateViewModel: PlayStateViewModel = ViewModelProvider.playState
) {
    val mediaList = playStateViewModel.songsList.collectAsState(initial = listOf())
    val playingSong = playStateViewModel.currentSongFlow.collectAsState(initial = null)

    Column {
        TitleBar()
        Divider(modifier = Modifier.fillMaxWidth(), thickness = .5.dp)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = nowPlaylistViewModel.listState,
            content = {
                itemsIndexed(mediaList.value) { _, item ->
                    MediaItem(song = item, isPlaying = playingSong.value == item, playStateViewModel)
                }
            })
    }
}


@Composable
private fun MediaItem(song: Song, isPlaying: Boolean, playStateViewModel: PlayStateViewModel) {
    Row(
        modifier = Modifier
            .clickable {
                playStateViewModel.play(song)
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.displayName,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.alpha(.5f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        AnimatedVisibility(visible = isPlaying) {
            SongIndicator(isPlaying)
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(onClick = { }, modifier = Modifier.width(24.dp)) {
            AsyncImage(model = R.drawable.ic_delete, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(22.dp))
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
        Text(
            text = stringResource(R.string.now_play_list),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun SongIndicator(isPlaying: Boolean) {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_music_indicator))
    LottieAnimation(
        modifier = Modifier
            .width(36.dp)
            .height(36.dp),
        isPlaying = isPlaying,
        composition = composition.value,
        speed = 2f,
        iterations = LottieConstants.IterateForever
    )
}
