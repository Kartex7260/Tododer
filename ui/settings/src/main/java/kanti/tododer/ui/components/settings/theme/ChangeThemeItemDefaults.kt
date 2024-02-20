package kanti.tododer.ui.components.settings.theme

object ChangeThemeItemDefaults {

    fun strings(
        appTheme: String = "App theme",
        themeAsSystem: String = "Like in the system",
        themeLight: String = "Light",
        themeDark: String = "Dark"
    ) = ChangeThemeItemStrings(
        appTheme = appTheme,
        themeAsSystem = themeAsSystem,
        themeLight = themeLight,
        themeDark = themeDark
    )
}