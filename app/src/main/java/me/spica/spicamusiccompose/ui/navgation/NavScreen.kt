package me.spica.spicamusiccompose.ui.navgation

import androidx.compose.runtime.Immutable

@Immutable
sealed class NavScreen(val route: String) {

    // 主页
    object Home : NavScreen("Home")

    object OnBoarding : NavScreen("OnBoarding")

}