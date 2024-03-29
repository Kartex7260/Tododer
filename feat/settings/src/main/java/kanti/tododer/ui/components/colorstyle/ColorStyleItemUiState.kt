package kanti.tododer.ui.components.colorstyle

import androidx.compose.runtime.Immutable

@Immutable
data class ColorStyleItemUiState(
    val currentStyleId: Int = 0,
    val colorStylesData: List<ColorStyleDataUiState> = listOf(ColorStyleDataUiState(0))
)
