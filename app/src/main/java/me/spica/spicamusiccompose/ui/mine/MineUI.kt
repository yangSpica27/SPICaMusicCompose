package me.spica.spicamusiccompose.ui.mine

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.persistence.entity.Song
import me.spica.spicamusiccompose.ui.common.ViewModelProvider
import me.spica.spicamusiccompose.ui.theme.GRAY3
import me.spica.spicamusiccompose.ui.viewmodel.LocalMusicViewModel
import me.spica.spicamusiccompose.ui.viewmodel.PlayStateViewModel


@Composable
fun MineUI(
    localMusicViewModel: LocalMusicViewModel = ViewModelProvider.localMusic,
    playStateViewModel: PlayStateViewModel = ViewModelProvider.playState
) {
    val localSongs = localMusicViewModel.songs.collectAsState(initial = listOf())
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                        .statusBarsPadding()
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = R.drawable.ic_box, contentDescription = null, modifier = Modifier.width(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.app_name))
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }



            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MineFuncButton(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }, icon = R.drawable.ic_cd_music_cd, title = stringResource(id = R.string.local), tintColor = Color(0xffFF9100)
                    )
                    MineFuncButton(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }, icon = R.drawable.ic_plus, title = stringResource(id = R.string.add_rencently), tintColor = Color(0xff3F51B5)
                    )
                    MineFuncButton(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }, icon = R.drawable.ic_collection_records, title = stringResource(id = R.string.collected), tintColor = Color(0xffF44336)
                    )
                    MineFuncButton(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }, icon = R.drawable.ic_shuffle_one, title = stringResource(id = R.string.shuffle), tintColor = Color(0xffD500F9)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                AnimatedVisibility(visible = localSongs.value.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "最近添加", style = MaterialTheme.typography.h6, modifier = Modifier
                                .alpha(.8f)
                                .padding(start = 22.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            item {
                                Spacer(modifier = Modifier.width(22.dp))
                            }
                            itemsIndexed(localSongs.value) { _, item ->
                                Row {
                                    SongCard(song = item, onClick = {
                                        playStateViewModel.play(item, localSongs.value)
                                    })
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                AnimatedVisibility(visible = localSongs.value.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "最近添加", style = MaterialTheme.typography.h6, modifier = Modifier
                                .alpha(.8f)
                                .padding(start = 22.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            item {
                                Spacer(modifier = Modifier.width(22.dp))
                            }
                            itemsIndexed(localSongs.value) { _, item ->
                                Row {
                                    SongCard(song = item, onClick = {
                                        playStateViewModel.play(item, localSongs.value)
                                    })
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun MineFuncButton(
    modifier: Modifier, @DrawableRes icon: Int, title: String, tintColor: Color = Color.Red
) {
    Box(
        modifier = modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .clip(CircleShape)
                    .background(GRAY3), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = icon, contentDescription = null, modifier = Modifier
                        .width(24.dp)
                        .height(24.dp), colorFilter = ColorFilter.tint(tintColor)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title, style = MaterialTheme.typography.button, modifier = Modifier.alpha(.8f)
            )
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SongCard(song: Song, onClick: () -> Unit) {
    val width = 108.dp
    Card(onClick = onClick) {
        Column(
            modifier = Modifier.width(width)
        ) {
            AsyncImage(
                model = song.getCoverUri(), contentDescription = null, modifier = Modifier
                    .width(width)
                    .height(width)
                    .background(GRAY3)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.displayName, style = MaterialTheme.typography.subtitle1, modifier = Modifier
                    .width(width)
                    .padding(horizontal = 8.dp), maxLines = 1
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .width(width)
                    .padding(horizontal = 8.dp)
                    .alpha(.6f),
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}