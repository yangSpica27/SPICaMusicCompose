package me.spica.spicamusiccompose.ui.navgation

import androidx.compose.runtime.Immutable

@Immutable
sealed class NavScreen(val route: String) {

    // 主页
    object Home : NavScreen("Home")

    // 引导
    object OnBoarding : NavScreen("OnBoarding")

    // 本地
    object LocalMusic : NavScreen("LocalMusic")

    // 收藏
    object Collect : NavScreen("Collect")

    // 歌单
    object Playlists:NavScreen("Playlists")

    // 搜索
    object SongSearch:NavScreen("SongSearch")

}