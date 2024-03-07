package kanti.tododer.data.model.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

	val settings: Flow<SettingsData>

	suspend fun setTheme(theme: AppTheme)

	suspend fun setColorStyle(colorStyleId: Int)

	suspend fun setMultiSelectionStyles(selection: Set<SelectionStyle>)

	suspend fun setGroupExpandDefault(expandDefault: Boolean)

	suspend fun resetColorStyle() {
		setColorStyle(COLOR_STYLE_DEFAULT)
	}

	suspend fun resetMultiSelectionStyle() {
		setMultiSelectionStyles(MULTI_SELECTION_STYLES_DEFAULT)
	}

	suspend fun resetGroupExpandDefault() {
		setGroupExpandDefault(GROUP_EXPAND_DEFAULT)
	}

	companion object {

		const val COLOR_STYLE_DEFAULT = -1
		val MULTI_SELECTION_STYLES_DEFAULT = setOf(SelectionStyle.ColorFill)
		const val GROUP_EXPAND_DEFAULT = true
	}
}