package me.spica.spicamusiccompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = ColorTheme,
    onError = ColorFail,
    secondary = Color2,
    secondaryVariant = Color3,
    onPrimary = Color.White,
    onBackground = Color.White,
    background = GRAY11
)

private val LightColorPalette = lightColors(
    primary = ColorTheme,
    onError = ColorFail,
    onBackground = Color.Black,
    secondary = Color2,
    secondaryVariant = Color3,
    onPrimary = Color.White,
    background = GRAY3,
    surface = Color.White
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}