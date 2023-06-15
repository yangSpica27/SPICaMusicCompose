package me.spica.spicamusiccompose.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.ui.common.ViewModelProvider
import me.spica.spicamusiccompose.ui.mine.MineUI
import me.spica.spicamusiccompose.ui.now_playlist.NowPlayListUI
import me.spica.spicamusiccompose.ui.player.PlayerUI

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeUI(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val playStateViewModel = ViewModelProvider.playState
    val mainListState = rememberLazyListState()
    val playingSong = playStateViewModel.currentSongFlow.collectAsState(initial = null)
    val playList = playStateViewModel.songsList.collectAsState(initial = listOf())

    Scaffold{ contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(Modifier.fillMaxSize()) {
                HorizontalPager(
                    pageCount = 3,
                    state = homeViewModel.pagerState,
                    userScrollEnabled = false
                ) { currentIndex ->
                    if ((homeViewModel.pagerState.currentPage - currentIndex) > 1) {
                        LoadingContent()
                    } else {
                        when (currentIndex) {
                            0 -> NowPlayListUI(listState = mainListState, songList = playList, playingSong = playingSong)
                            1 -> PlayerUI(currentSong = playingSong)
                            2 -> MineUI()
                        }
                    }
                }
            }
            BottomNavBar(homeViewModel)
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "加载中..")

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomNavBar(homeViewModel: HomeViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(
                start = 14.dp,
                end = 14.dp
            )
            .navigationBarsPadding()
    ) {
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavButton(
                    isSelected = homeViewModel.pagerState.currentPage == 0,
                    icon = R.drawable.ic_current_list,
                    name = stringResource(id = R.string.now_play_list),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            coroutineScope.launch {
                                homeViewModel.selectBottomNav(0)
                            }
                        }
                )
                NavButton(
                    isSelected = homeViewModel.pagerState.currentPage == 1,
                    icon = R.drawable.ic_nav_player,
                    name = stringResource(id = R.string.now_playing),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = {
                            coroutineScope.launch {
                                homeViewModel.selectBottomNav(1)
                            }
                        })
                )
                NavButton(
                    isSelected = homeViewModel.pagerState.currentPage == 2,
                    icon = R.drawable.ic_mine,
                    name = stringResource(id = R.string.mine),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            coroutineScope.launch {
                                homeViewModel.selectBottomNav(2)
                            }
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun NavButton(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.ic_current_list, name: String = "标题",
    isSelected: Boolean
) {
    Box(
        modifier = modifier
            .height(72.dp)
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = icon, contentDescription = null, colorFilter = if (isSelected) {
                    ColorFilter.tint(MaterialTheme.colorScheme.primary)
                } else {
                    ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                },
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = name, style = MaterialTheme.typography.bodySmall, color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )
        }
    }

}