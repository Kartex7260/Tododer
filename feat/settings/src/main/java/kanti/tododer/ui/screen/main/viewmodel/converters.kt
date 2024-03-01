package kanti.tododer.ui.screen.main.viewmodel

import kanti.tododer.data.colorstyle.ColorStyleData
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.model.settings.AppTheme
import kanti.tododer.ui.components.colorstyle.ColorStyleDataType
import kanti.tododer.ui.components.colorstyle.ColorStyleDataUiState
import kanti.tododer.ui.components.theme.ThemeSettingsUiState

fun AppTheme.toUiState(): ThemeSettingsUiState {
	return when (this) {
		AppTheme.AS_SYSTEM -> ThemeSettingsUiState.AS_SYSTEM
		AppTheme.LIGHT -> ThemeSettingsUiState.LIGHT
		AppTheme.DARK -> ThemeSettingsUiState.DARK
	}
}

fun ThemeSettingsUiState.toData(): AppTheme {
	return when (this) {
		ThemeSettingsUiState.AS_SYSTEM -> AppTheme.AS_SYSTEM
		ThemeSettingsUiState.LIGHT -> AppTheme.LIGHT
		ThemeSettingsUiState.DARK -> AppTheme.DARK
	}
}

fun ColorStyleData.toUiState(): ColorStyleDataUiState {
	return ColorStyleDataUiState(
		id = id,
		name = name,
		type = type.toUiState()
	)
}

fun ColorStyleType.toUiState(): ColorStyleDataType = when (this) {
	ColorStyleType.Standard -> ColorStyleDataType.Standard
	ColorStyleType.Dynamic -> ColorStyleDataType.Dynamic
	ColorStyleType.Red -> ColorStyleDataType.Red
	ColorStyleType.Orange -> ColorStyleDataType.Orange
	ColorStyleType.Yellow -> ColorStyleDataType.Yellow
	ColorStyleType.Green -> ColorStyleDataType.Green
	ColorStyleType.LightBlue -> ColorStyleDataType.LightBlue
	ColorStyleType.Blue -> ColorStyleDataType.Blue
	ColorStyleType.Purple -> ColorStyleDataType.Purple
	ColorStyleType.Custom -> ColorStyleDataType.Custom
}