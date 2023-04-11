package me.spica.spicamusiccompose.ui.on_boarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun OnBoarding() {
    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        Card(modifier = Modifier.fillMaxSize()) {
            Text(text = "授予权限", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }
    }
}