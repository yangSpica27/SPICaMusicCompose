package me.spica.spicamusiccompose.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song

@Composable
fun PlayerUI(
    currentSong: State<Song?>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Crossfade(targetState = currentSong.value == null) { isEmpty ->
            if (isEmpty) {
                EmptyContent()
            } else {
                PlayContent(currentSong = currentSong)
            }
        }
    }
}

@Composable
private fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.plates_empty_re_opql,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = { }) {
            Text(
                text = stringResource(id = R.string.no_playing),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun PlayContent(currentSong: State<Song?>) {
    val configuration = LocalConfiguration.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        TitleBar(song = currentSong.value)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(configuration.screenWidthDp.dp)
                .height(configuration.screenWidthDp.dp)
                .padding(horizontal = 22.dp)
        ) {
            Card(modifier = Modifier.fillMaxSize()) {

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        SeekContent(song = currentSong.value, positionDs = 0, isPlay = false)
        ControllerContent()
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SeekContent(
    song: Song?,
    positionDs: Long,
    isPlay: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 22.dp, vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "00:00")
        Spacer(modifier = Modifier.width(8.dp))
        Slider(value = 0f, onValueChange = {}, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "00:00")
    }
}

@Composable
private fun ControllerContent() {
    Row {
        IconButton(
            onClick = { },
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(CircleShape)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = R.drawable.ic_pre,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = { },
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = R.drawable.ic_play,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = { },
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(CircleShape)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = R.drawable.ic_next,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
private fun TitleBar(song: Song?) {
    AnimatedVisibility(visible = song != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = song?.displayName ?: "歌曲名称", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = song?.artist ?: "作曲家", style = MaterialTheme.typography.titleSmall, modifier = Modifier.alpha(.7f))
            }
            IconButton(onClick = { }) {
                AsyncImage(
                    model = R.drawable.ic_acoustic,
                    contentDescription = null,
                    modifier = Modifier.width(24.dp)
                )
            }
        }
    }
}