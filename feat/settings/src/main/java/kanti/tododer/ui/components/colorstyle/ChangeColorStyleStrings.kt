package kanti.tododer.ui.components.colorstyle

import androidx.compose.runtime.Immutable

@Immutable
data class ChangeColorStyleStrings(
    val colorStyle: String,
    val standard: String,
    val dynamic: String,
    val red: String,
    val orange: String,
    val yellow: String,
    val green: String,
    val lightBlue: String,
    val blue: String,
    val purple: String
) {

    fun getByType(type: ColorStyleDataType): String? {
        return when (type) {
            ColorStyleDataType.Standard -> standard
            ColorStyleDataType.Dynamic -> dynamic
            ColorStyleDataType.Red -> red
            ColorStyleDataType.Orange -> orange
            ColorStyleDataType.Yellow -> yellow
            ColorStyleDataType.Green -> green
            ColorStyleDataType.LightBlue -> lightBlue
            ColorStyleDataType.Blue -> blue
            ColorStyleDataType.Purple -> purple
            ColorStyleDataType.Custom -> null
        }
    }
}
