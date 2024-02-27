package kanti.tododer.ui.common

import javax.annotation.concurrent.Immutable

@Immutable
data class PlansUiState(
    val selection: Boolean = false,
    val plans: List<PlanUiState> = listOf()
)
