package kanti.tododer.ui.screen.main.viewmodel

import kanti.tododer.ui.components.settings.ThemeSettingsUiState

data class SettingsMainUiState(
	val appTheme: ThemeSettingsUiState = ThemeSettingsUiState.AS_SYSTEM
)
