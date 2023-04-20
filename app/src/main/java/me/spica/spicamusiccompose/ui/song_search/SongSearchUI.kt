package me.spica.spicamusiccompose.ui.song_search

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.ui.theme.GRAY3


@Composable
fun SongSearchUI(searchViewModel: SongSearchViewModel = hiltViewModel()) {

    val songsState = searchViewModel.songs.collectAsState(initial = null)

    Scaffold(backgroundColor = GRAY3) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            SearchBar(searchViewModel = searchViewModel)
            Box(modifier = Modifier.weight(1f)) {
                if (songsState.value == null) {
                    LoadingPage()
                } else if (songsState.value.isNullOrEmpty()) {
                    EmptyPage()
                } else {
                    ListPage(songsState = songsState)
                }
            }
        }
    }
}

@Composable
private fun EmptyPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = R.drawable.plates_select_re_3kbd, contentDescription = null)
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = { }) {
            Text(text = stringResource(id = R.string.no_data))
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
                text = stringResource(R.string.is_loading), style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@Composable
private fun SearchBar(searchViewModel: SongSearchViewModel) {
    val state = remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
                searchViewModel.search(value.text)
            },
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (state.value != TextFieldValue("")) {
                    IconButton(
                        onClick = {
                            state.value =
                                TextFieldValue("")
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
            ),
            placeholder = {
                Text(text = stringResource(R.string.pls_enter_keyword), color = Color.DarkGray)
            }
        )
    }

}

@Composable
private fun ListPage(songsState: State<List<Song>?>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(songsState.value ?: listOf()) {
            Column {
                SongItem(song = it)
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
private fun SongItem(song: Song) {
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
                .background(GRAY3),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.displayName, style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = song.artist, style = MaterialTheme.typography.subtitle2, modifier = Modifier.alpha(.5f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = {

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
                .background(GRAY3)
                .padding(4.dp)
        ) {
            AsyncImage(model = R.drawable.ic_more, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(22.dp))
    }
}