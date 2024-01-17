package kanti.tododer.ui.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.feat.settings.R
import kanti.tododer.ui.components.settings.ChangeThemeItem
import kanti.tododer.ui.screen.main.viewmodel.SettingsMainViewModel
import kanti.tododer.ui.screen.main.viewmodel.SettingsMainViewModelImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsMainScreenTopBar(
	scrollBehaviour: TopAppBarScrollBehavior,
	back: () -> Unit
) {
	TopAppBar(
		title = { Text(text = stringResource(id = R.string.settings)) },
		navigationIcon = {
			IconButton(onClick = { back() }) {
				Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
			}
		},
		scrollBehavior = scrollBehaviour
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainScreen(
	navController: NavController = rememberNavController(),
	vm: SettingsMainViewModel = hiltViewModel<SettingsMainViewModelImpl>()
) {
	val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
	val uiState by vm.uiState.collectAsState()
	Scaffold(
		modifier = Modifier
			.nestedScroll(connection = scrollBehaviour.nestedScrollConnection),

		topBar = {
			SettingsMainScreenTopBar(
				scrollBehaviour = scrollBehaviour,
				back = { navController.popBackStack() }
			)
		}
	) { paddingValues ->
		LazyColumn(
			modifier = Modifier.padding(paddingValues)
		) {
			item {
				ChangeThemeItem(
					state = uiState.appTheme,
					onThemeChanged = { vm.changeAppTheme(it) }
				)
			}
		}
	}
}

@Preview
@Composable
fun PreviewSettingsMainScreen() {
	SettingsMainScreen(
		vm = SettingsMainViewModel
	)
}