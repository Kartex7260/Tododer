package kanti.tododer.ui.screen.main.viewmodel

import kanti.tododer.ui.components.settings.theme.ThemeSettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SettingsMainViewModel {

	val uiState: StateFlow<SettingsMainUiState>

	fun changeAppTheme(theme: ThemeSettingsUiState)

	companion object : SettingsMainViewModel {

		private val _uiState = MutableStateFlow(SettingsMainUiState())
		override val uiState: StateFlow<SettingsMainUiState> = _uiState.asStateFlow()

		override fun changeAppTheme(theme: ThemeSettingsUiState) {
			_uiState.update {
				it.copy(
					appTheme = theme
				)
			}
		}
	}
}