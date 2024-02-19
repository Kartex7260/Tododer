package kanti.tododer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleRepository
import kanti.tododer.data.model.settings.AppTheme
import kanti.tododer.data.model.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	settingsRepository: SettingsRepository,
	colorStyleRepository: ColorStyleRepository
) : ViewModel() {

	val appTheme: StateFlow<AppTheme> = settingsRepository.settings
		.map {
			it.appTheme
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = AppTheme.AS_SYSTEM
		)

	val colorStyle: StateFlow<ColorStyle?> = settingsRepository.settings
		.map { settingsData ->
			settingsData.colorStyleId?.let { colorStyleRepository.getById(it) }
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = null
		)
}