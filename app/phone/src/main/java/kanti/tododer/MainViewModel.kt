package kanti.tododer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.settings.AppTheme
import kanti.tododer.data.model.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	settingsRepository: SettingsRepository
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
}