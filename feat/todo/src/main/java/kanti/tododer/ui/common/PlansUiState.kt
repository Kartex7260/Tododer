package kanti.tododer.ui.common

data class PlansUiState(
    val selection: Boolean = false,
    val plans: List<PlanUiState> = listOf()
)
