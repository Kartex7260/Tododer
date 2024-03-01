package kanti.tododer.ui.screen.main.viewmodel

import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.components.colorstyle.ColorStyleDataType
import kanti.tododer.ui.components.colorstyle.ColorStyleDataUiState
import kanti.tododer.ui.components.colorstyle.ColorStyleItemUiState
import kanti.tododer.ui.components.theme.ThemeSettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SettingsMainViewModel {

	val uiState: StateFlow<SettingsMainUiState>

	fun changeAppTheme(theme: ThemeSettingsUiState)

	fun changeColorStyle(colorStyleId: Int)

	fun changeSelectionStyles(styles: Set<MultiSelectionStyle>)

	companion object : SettingsMainViewModel {

		private val _uiState = MutableStateFlow(SettingsMainUiState(
			colorStyle = ColorStyleItemUiState(
				currentStyleId = 1,
				colorStylesData = listOf(
					ColorStyleDataUiState(1, "Black"),
					ColorStyleDataUiState(2, "Foo"),
					ColorStyleDataUiState(3, "", ColorStyleDataType.Dynamic),
					ColorStyleDataUiState(4, "", ColorStyleDataType.Red)
				)
			)
		))
		override val uiState: StateFlow<SettingsMainUiState> = _uiState.asStateFlow()

		override fun changeAppTheme(theme: ThemeSettingsUiState) {
			_uiState.update {
				it.copy(
					appTheme = theme
				)
			}
		}

		override fun changeColorStyle(colorStyleId: Int) {
			_uiState.update {
				val colorStyle = it.colorStyle
				it.copy(
					colorStyle = colorStyle.copy(
						currentStyleId = colorStyleId
					)
				)
			}
		}

		override fun changeSelectionStyles(styles: Set<MultiSelectionStyle>) {
		}
	}
}