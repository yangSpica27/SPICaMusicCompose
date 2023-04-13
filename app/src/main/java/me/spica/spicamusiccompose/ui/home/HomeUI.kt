package me.spica.spicamusiccompose.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.ui.mine.MineUI
import me.spica.spicamusiccompose.ui.now_playlist.NowPlayListUI
import me.spica.spicamusiccompose.ui.now_playlist.NowPlaylistViewModel
import me.spica.spicamusiccompose.ui.player.PlayerUI

@Composable
fun HomeUI(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val selectIndexState = remember { homeViewModel.selectIndex }

    val nowPlaylistViewModel: NowPlaylistViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            Column {
                Divider(modifier = Modifier.fillMaxWidth(), thickness = .5.dp)
                BottomNavBar(homeViewModel)
            }
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column {
                Crossfade(
                    selectIndexState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { dest ->
                    when (dest.value) {
                        0 -> NowPlayListUI(nowPlaylistViewModel = nowPlaylistViewModel)
                        1 -> PlayerUI()
                        2 -> MineUI()
                    }
                }

            }
        }
    }


}


@Composable
private fun BottomNavBar(homeViewModel: HomeViewModel) {
    val selectIndexState = remember { homeViewModel.selectIndex }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavButton(
            isSelected = selectIndexState.value == 0,
            icon = R.drawable.ic_current_list,
            name = stringResource(id = R.string.now_play_list),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    homeViewModel.selectBottomNav(0)
                }
        )
        NavButton(
            isSelected = selectIndexState.value == 1,
            icon = R.drawable.ic_nav_player,
            name = stringResource(id = R.string.now_playing),
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { homeViewModel.selectBottomNav(1) })
        )
        NavButton(
            isSelected = selectIndexState.value == 2,
            icon = R.drawable.ic_mine,
            name = stringResource(id = R.string.mine),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    homeViewModel.selectBottomNav(2)
                }
        )
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
                    ColorFilter.tint(MaterialTheme.colors.primary)
                } else {
                    ColorFilter.tint(MaterialTheme.colors.onSurface)
                },
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = name, style = MaterialTheme.typography.button, color = if (isSelected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onSurface
                }
            )
        }
    }

}