package kanti.tododer.ui.screen.main.viewmodel

import androidx.compose.runtime.Immutable
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.components.colorstyle.ColorStyleItemUiState
import kanti.tododer.ui.components.theme.ThemeSettingsUiState

@Immutable
data class SettingsMainUiState(
	val appTheme: ThemeSettingsUiState = ThemeSettingsUiState.AS_SYSTEM,
	val colorStyle: ColorStyleItemUiState = ColorStyleItemUiState(),
	val selectionStyles: Set<MultiSelectionStyle> = setOf(),
	val groupExpandDefault: Boolean = true
)
