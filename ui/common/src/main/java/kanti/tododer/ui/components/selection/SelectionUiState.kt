package kanti.tododer.ui.components.selection

data class SelectionUiState(
    val selection: Boolean = false,
    val selected: Set<Long> = setOf()
)
