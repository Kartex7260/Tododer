package kanti.tododer.ui.components.settings.colorstyle

data class ColorStyleDataUiState(
    val id: Int = 0,
    val name: String = "",
    val type: ColorStyleDataType = ColorStyleDataType.Custom
)

enum class ColorStyleDataType {

    Standard,
    Dynamic,

    Red,
    Orange,
    Yellow,
    Green,
    LightBlue,
    Blue,
    Purple,

    Custom
}
