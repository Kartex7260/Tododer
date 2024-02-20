package kanti.tododer.ui.screen.main.viewmodel

import kanti.tododer.ui.components.settings.colorstyle.ColorStyleItemUiState
import kanti.tododer.ui.components.settings.theme.ThemeSettingsUiState

data class SettingsMainUiState(
	val appTheme: ThemeSettingsUiState = ThemeSettingsUiState.AS_SYSTEM,
	val colorStyle: ColorStyleItemUiState = ColorStyleItemUiState()
)
