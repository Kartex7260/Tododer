package kanti.tododer.data.model.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

	val settings: Flow<SettingsData>

	suspend fun setTheme(theme: AppTheme)

	suspend fun setColorStyle(colorStyleId: Int)

	suspend fun setMultiSelectionStyles(selection: Set<SelectionStyle>)

	suspend fun resetColorStyle() {
		setColorStyle(COLOR_STYLE_DEFAULT)
	}

	suspend fun resetMultiSelectionStyle() {
		setMultiSelectionStyles(setOf(SelectionStyle.ColorFill))
	}

	companion object {

		const val COLOR_STYLE_DEFAULT = -1
	}
}