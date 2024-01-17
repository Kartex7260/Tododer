package kanti.tododer.data.model.settings

import kanti.tododer.ui.components.settings.ThemeSettingsUiState

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