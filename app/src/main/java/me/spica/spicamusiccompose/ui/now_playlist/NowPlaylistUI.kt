package me.spica.spicamusiccompose.ui.now_playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    listState: LazyListState,
    songList: State<List<Song>>,
    playingSong: State<Song?>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = songList.value.isEmpty()) { isEmpty ->
            if (isEmpty) {
                EmptyPage()
            } else {
                SongList(
                    listState = listState, songList = songList, playingSong = playingSong
                )
            }
        }

    }
}

@Composable
fun SongList(
    listState: LazyListState, songList: State<List<Song>>, playingSong: State<Song?>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        ListHeader(playingSong = playingSong)
        LazyColumn(modifier = Modifier
            .background(Color.White)
            .weight(1f)
            .statusBarsPadding(),
            state = listState, content = {
                itemsIndexed(songList.value) { _, item ->
                    MediaItem(song = item, isPlaying = playingSong.value == item)
                }
            })
    }

}

@Composable
private fun ListHeader(playingSong: State<Song?>) {
    AnimatedVisibility(visible = playingSong.value != null) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = playingSong.value?.getCoverUri(),
                    contentDescription = null,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = playingSong.value?.displayName ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = playingSong.value?.artist ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    Modifier.align(Alignment.CenterStart)
                ) {
                    Text(text = "0/0", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp))
                }
                Box(
                    Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(id = R.string.now_play_list), style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                    )
                }
                Box(
                    Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(text = stringResource(R.string.clear), style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(thickness = .5.dp)
        }
    }
}


@Composable
private fun MediaItem(
    song: Song, isPlaying: Boolean, playStateViewModel: PlayStateViewModel = ViewModelProvider.playState
) {

    Row(
        modifier = Modifier
            .clickable {
                playStateViewModel.play(song)
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
                text = song.artist, style = MaterialTheme.typography.titleSmall, modifier = Modifier.alpha(.5f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        AnimatedVisibility(visible = isPlaying) {
            SongIndicator(isPlaying)
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = { }, modifier = Modifier
                .width(34.dp)
                .height(34.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = R.drawable.ic_delete,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
            )
        }
        Spacer(modifier = Modifier.width(22.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleBar() {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(
                text = stringResource(R.string.now_play_list), style = MaterialTheme.typography.titleMedium
            )
        }
    )
}

@Composable
fun SongIndicator(isPlaying: Boolean) {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_song_indicator))
    LottieAnimation(
        modifier = Modifier
            .width(36.dp)
            .height(36.dp), isPlaying = isPlaying, composition = composition.value, iterations = LottieConstants.IterateForever
    )
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
            Text(text = stringResource(id = R.string.playlist_select))
        }
    }
}