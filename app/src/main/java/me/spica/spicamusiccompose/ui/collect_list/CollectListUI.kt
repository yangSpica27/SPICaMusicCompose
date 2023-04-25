package me.spica.spicamusiccompose.ui.collect_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel

/**  收藏列表 **/
@Composable
fun CollectListUI(collectListViewModel: CollectListViewModel = hiltViewModel()) {

    val songState = collectListViewModel.songs.collectAsState(initial = null)

    val scope = rememberCoroutineScope()





}