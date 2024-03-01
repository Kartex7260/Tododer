package kanti.tododer.data.model.settings

data class SettingsData(
	val appTheme: AppTheme = AppTheme.AS_SYSTEM,
	val colorStyleId: Int = 0,
	val multiSelectionStyleFlags: Int = 0
)
