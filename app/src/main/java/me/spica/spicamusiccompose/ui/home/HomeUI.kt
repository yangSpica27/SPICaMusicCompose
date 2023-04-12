package me.spica.spicamusiccompose.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import me.spica.spicamusiccompose.utils.PermissionUtils
import me.spica.spicamusiccompose.ui.navgation.NavScreen
import me.spica.spicamusiccompose.ui.navgation.Navigator

@Composable
fun HomeUI() {

    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        Card(modifier = Modifier.fillMaxSize()) {
            Text(text = "首页", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }
    }


}