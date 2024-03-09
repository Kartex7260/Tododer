package kanti.tododer.ui.components.selection

import javax.annotation.concurrent.Immutable

@Immutable
data class SelectionUiState(
    val selection: Boolean = false,
    val selected: Set<Long> = setOf()
)
