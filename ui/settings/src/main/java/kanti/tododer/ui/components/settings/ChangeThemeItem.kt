package kanti.tododer.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChangeThemeItem(
	modifier: Modifier = Modifier,
	state: ThemeSettingsUiState,
	onThemeChanged: ((ThemeSettingsUiState) -> Unit) = {}
) {
	val appThemeAsSystem = stringResource(id = R.string.app_theme_as_system)
	val appThemeLight = stringResource(id = R.string.app_theme_light)
	val appThemeDark = stringResource(id = R.string.app_theme_dark)

	var expandThemeMenu by rememberSaveable { mutableStateOf(false) }
	ListItem(
		modifier = modifier
			.clickable { expandThemeMenu = true },
		headlineContent = { Text(text = stringResource(id = R.string.app_theme)) },
		supportingContent = {
			Text(
				text = when (state) {
					ThemeSettingsUiState.AS_SYSTEM -> appThemeAsSystem
					ThemeSettingsUiState.LIGHT -> appThemeLight
					ThemeSettingsUiState.DARK -> appThemeDark
				}
			)
		},
		leadingContent = {
			Icon(
				painter = painterResource(id = R.drawable.baseline_dark_mode_24),
				contentDescription = null
			)
		}
	)
	val closeMenu = {
		expandThemeMenu = false
	}
	DropdownMenu(expanded = expandThemeMenu, onDismissRequest = { closeMenu() }) {
		DropdownMenuItem(
			text = { Text(text = appThemeAsSystem) },
			onClick = {
				onThemeChanged(ThemeSettingsUiState.AS_SYSTEM)
				closeMenu()
			}
		)
		DropdownMenuItem(
			text = { Text(text = appThemeLight) },
			onClick = {
				onThemeChanged(ThemeSettingsUiState.LIGHT)
				closeMenu()
			}
		)
		DropdownMenuItem(
			text = { Text(text = appThemeDark) },
			onClick = {
				onThemeChanged(ThemeSettingsUiState.DARK)
				closeMenu()
			}
		)
	}
}

@Preview
@Composable
private fun PreviewThemeSettingsItem() {
	var theme by remember {
		mutableStateOf(ThemeSettingsUiState.AS_SYSTEM)
	}
	ChangeThemeItem(
		state = theme,
		onThemeChanged = { theme = it }
	)
}