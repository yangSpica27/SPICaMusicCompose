package me.spica.spicamusiccompose.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.spica.spicamusiccompose.ui.common.ProvideMultiViewModel
import me.spica.spicamusiccompose.ui.home.HomeUI
import me.spica.spicamusiccompose.ui.navgation.NavScreen
import me.spica.spicamusiccompose.ui.navgation.Navigator
import me.spica.spicamusiccompose.ui.navgation.ProvideNavHostController
import me.spica.spicamusiccompose.ui.on_boarding.OnBoardingUI

@Composable
fun MainUI() {
    Scaffold { contentPadding ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            // 路由
            ProvideMultiViewModel {
                ProvideNavHostController {
                    NavHost(
                        navController = Navigator.current,
                        // 默认进入的页面
                        startDestination = NavScreen.OnBoarding.route
                    ) {
                        composable(route = NavScreen.Home.route) {
                            HomeUI()
                        }
                        composable(route = NavScreen.OnBoarding.route) {
                            OnBoardingUI()
                        }
                    }
                }
            }

        }
    }


}