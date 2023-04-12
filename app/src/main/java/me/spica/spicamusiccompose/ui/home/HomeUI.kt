package me.spica.spicamusiccompose.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.ui.main.HomeViewModel
import me.spica.spicamusiccompose.ui.mine.MineUI
import me.spica.spicamusiccompose.ui.now_playlist.NowPlayListUI
import me.spica.spicamusiccompose.ui.player.PlayerUI

@Composable
fun HomeUI(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val selectIndexState = remember { homeViewModel.selectIndex }

    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        Column {
            Crossfade(
                selectIndexState, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { dest ->
                when (dest.value) {
                    0 -> {
                        NowPlayListUI()
                    }
                    2 -> {
                        PlayerUI()
                    }
                    3 -> {
                        MineUI()
                    }
                }
            }
            BottomNavBar(homeViewModel)
        }
    }
}


@Composable
private fun BottomNavBar(homeViewModel: HomeViewModel) {
    val selectIndexState = remember { homeViewModel.selectIndex }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            homeViewModel.selectBottomNav(0)
        }, modifier = Modifier.weight(1f)) {
            if (selectIndexState.value == 0) {
                Text(text = "0-选中")
            } else {
                Text(text = "0")
            }
        }

        TextButton(onClick = {
            homeViewModel.selectBottomNav(1)
        }, modifier = Modifier.weight(1f)) {
            if (selectIndexState.value == 1) {
                Text(text = "1-选中")
            } else {
                Text(text = "1")
            }
        }
        TextButton(onClick = {
            homeViewModel.selectBottomNav(2)
        }, modifier = Modifier.weight(1f)) {
            if (selectIndexState.value == 2) {
                Text(text = "2选中")
            } else {
                Text(text = "2")
            }
        }
    }
}