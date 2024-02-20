package kanti.tododer.ui.components.settings.theme

import androidx.compose.runtime.Stable

@Stable
data class ChangeThemeItemStrings(
    internal val appTheme: String,
    internal val themeAsSystem: String,
    internal val themeLight: String,
    internal val themeDark: String
)
