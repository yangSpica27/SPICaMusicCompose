package me.spica.spicamusiccompose.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import me.spica.spicamusiccompose.ui.viewmodel.DspViewModel
import me.spica.spicamusiccompose.ui.viewmodel.LocalMusicViewModel
import me.spica.spicamusiccompose.ui.viewmodel.PlayStateViewModel

object ViewModelProvider {
    val localMusic: LocalMusicViewModel
        @Composable
        get() = LocalPodcastSearchViewModel.current

    val dsp: DspViewModel
        @Composable
        get() = LocalPodcastDetailViewModel.current

    val playState: PlayStateViewModel
        @Composable
        get() = LocalPodcastPlayerViewModel.current
}

@Composable
fun ProvideMultiViewModel(content: @Composable () -> Unit) {
    val viewModel1: LocalMusicViewModel = viewModel()
    val viewModel2: DspViewModel = viewModel()
    val viewModel3: PlayStateViewModel = viewModel()

    CompositionLocalProvider(
        LocalPodcastSearchViewModel provides viewModel1,
    ) {
        CompositionLocalProvider(
            LocalPodcastDetailViewModel provides viewModel2,
        ) {
            CompositionLocalProvider(
                LocalPodcastPlayerViewModel provides viewModel3,
            ) {
                content()
            }
        }
    }
}

private val LocalPodcastSearchViewModel = staticCompositionLocalOf<LocalMusicViewModel> {
    error("No LocalMusicViewModel provided")
}

private val LocalPodcastDetailViewModel = staticCompositionLocalOf<DspViewModel> {
    error("No DspViewModel provided")
}

private val LocalPodcastPlayerViewModel = staticCompositionLocalOf<PlayStateViewModel> {
    error("No PlayStateViewModel provided")
}