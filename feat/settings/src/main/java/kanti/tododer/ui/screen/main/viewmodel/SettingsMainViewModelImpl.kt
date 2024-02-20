package kanti.tododer.ui.screen.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.settings.SettingsRepository
import kanti.tododer.data.model.settings.toData
import kanti.tododer.data.model.settings.toUiState
import kanti.tododer.ui.components.settings.theme.ThemeSettingsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModelImpl @Inject constructor(
	private val settingsRepository: SettingsRepository
): ViewModel(), SettingsMainViewModel {

	override val uiState: StateFlow<SettingsMainUiState> = settingsRepository.settings
		.map { settingsData ->
			SettingsMainUiState(
				appTheme = settingsData.appTheme.toUiState()
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = SettingsMainUiState()
		)

	override fun changeAppTheme(theme: ThemeSettingsUiState) {
		viewModelScope.launch {
			settingsRepository.setTheme(theme.toData())
		}
	}
}