package me.spica.spicamusiccompose.ui.on_boarding

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.ui.navgation.NavScreen
import me.spica.spicamusiccompose.ui.navgation.Navigator


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingUI(
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
) {

    val pagerState = rememberPagerState()

    val isSuccess = remember { onBoardingViewModel.isSuccess }

    if (isSuccess.value) {
        Navigator.current.navigate(NavScreen.Home.route) {
            popUpTo(NavScreen.OnBoarding.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        HorizontalPager(
            pageCount = 2,
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false
        ) { index ->
            if (index == 0) {
                StoragePermissionPager(pagerState = pagerState)
            } else {
                ScannerPager(onBoardingViewModel)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionPager(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    val permissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(22.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.lets_go),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AsyncImage(
                        model = R.drawable.plates_confirmation_re_b6q5,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.please_give_permisstion_help_text),
                        style = MaterialTheme.typography.titleMedium, modifier = Modifier.alpha(.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            if (permissionState.status.isGranted) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }, modifier = Modifier
                        .padding(horizontal = 22.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.contine_to_scanner))
                }
            } else {
                Button(
                    onClick = {
                        permissionState.launchPermissionRequest()
                    }, modifier = Modifier
                        .padding(horizontal = 22.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.requset_permission))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

        }


    }
}

@Composable
fun ScannerPager(viewModel: OnBoardingViewModel) {
    val isScanner = rememberSaveable { viewModel.isScanner }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Crossfade(targetState = isScanner.value) { isScanner ->
                    if (isScanner) {
                        ScannerWidget(viewModel = viewModel)
                    } else {
                        Column(Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = R.drawable.plates_not_found_re_bh2e,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                    }
                }
                ScannerWidget(viewModel = viewModel)
            }

            AnimatedVisibility(visible = !isScanner.value) {
                Button(
                    onClick = {
                        viewModel.scannerSongs()
                    }, modifier = Modifier
                        .padding(horizontal = 22.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.scanner))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScannerWidget(viewModel: OnBoardingViewModel) {

    val isScannerState = rememberSaveable { viewModel.isScanner }

    val scannerProgressState = remember { viewModel.scannerProgressState }
    val animateProgress = animateFloatAsState(
        targetValue = scannerProgressState.value.progress?.asFloat() ?: 0f,
        animationSpec = tween()
    )
    if (isScannerState.value) {
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (isScannerState.value) {
                    "导入中.."
                } else {
                    "准备中.."
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = animateProgress.value,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = scannerProgressState.value.message,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    } else {
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "可以扫描了!", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }

}