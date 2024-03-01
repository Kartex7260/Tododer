package kanti.tododer.ui.screen.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.colorstyle.ColorStyleRepository
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.model.settings.SettingsRepository
import kanti.tododer.ui.components.colorstyle.ColorStyleItemUiState
import kanti.tododer.ui.components.theme.ThemeSettingsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModelImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val colorStyleRepository: ColorStyleRepository
) : ViewModel(), SettingsMainViewModel {

    override val uiState: StateFlow<SettingsMainUiState> = settingsRepository.settings
        .map { settingsData ->
            SettingsMainUiState(
                appTheme = settingsData.appTheme.toUiState(),
                colorStyle = ColorStyleItemUiState(
                    currentStyleId = settingsData.colorStyleId,
                    colorStylesData = colorStyleRepository.getAllColorStyleData()
                        .filter {
                            if (it.type == ColorStyleType.Dynamic &&
                                !DynamicColors.isDynamicColorAvailable())
                                return@filter false
                            true
                        }
                        .map {
                            it.toUiState()
                        }
                )
            )
        }
        .flowOn(Dispatchers.Default)
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

    override fun changeColorStyle(colorStyleId: Int) {
        viewModelScope.launch {
            settingsRepository.setColorStyle(colorStyleId)
        }
    }
}