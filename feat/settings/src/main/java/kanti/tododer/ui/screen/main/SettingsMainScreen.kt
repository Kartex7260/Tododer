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
import kanti.tododer.ui.components.colorstyle.ChangeColorStyleItem
import kanti.tododer.ui.components.colorstyle.ChangeColorStyleItemDefaults
import kanti.tododer.ui.components.multiselection.SelectionItem
import kanti.tododer.ui.components.multiselection.SelectionMenuItemsDefaults
import kanti.tododer.ui.components.theme.ChangeThemeItem
import kanti.tododer.ui.components.theme.ChangeThemeItemDefaults
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
					strings = ChangeThemeItemDefaults.strings(
						appTheme = stringResource(id = R.string.app_theme),
						themeAsSystem = stringResource(id = R.string.app_theme_as_system),
						themeLight = stringResource(id = R.string.app_theme_light),
						themeDark = stringResource(id = R.string.app_theme_dark)
					),
					onThemeChanged = { vm.changeAppTheme(it) }
				)
			}
			item {
				ChangeColorStyleItem(
					state = uiState.colorStyle,
					strings = ChangeColorStyleItemDefaults.strings(
						colorStyle = stringResource(id = R.string.color_style),
						standard = stringResource(id = R.string.app_name),
						dynamic = stringResource(id = R.string.color_style_dynamic),
						red = stringResource(id = R.string.color_style_red),
						orange = stringResource(id = R.string.color_style_orange),
						yellow = stringResource(id = R.string.color_style_yellow),
						green = stringResource(id = R.string.color_style_green),
						lightBlue = stringResource(id = R.string.color_style_light_blue),
						blue = stringResource(id = R.string.color_style_blue),
						purple = stringResource(id = R.string.color_style_purple)
					),
					onChangeStyle = { id ->  vm.changeColorStyle(id) }
				)
			}
			item {
				SelectionItem(
					strings = SelectionMenuItemsDefaults.strings(
						selectionStyle = stringResource(id = R.string.multi_selection_style),
						colorFill = stringResource(id = R.string.color_fill),
						checkbox = stringResource(id = R.string.checkbox),
						setSelectionStyle = stringResource(id = R.string.set_multi_selection_style),
						close = stringResource(id = R.string.close)
					),
					selectionStyles = uiState.selectionStyles,
					onSetSelectionStyles = { vm.changeSelectionStyles(it) }
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