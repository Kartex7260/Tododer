package kanti.tododer.ui.common

import kanti.tododer.ui.components.plan.PlanData

data class PlanUiState(
    val selected: Boolean = false,
    val visible: Boolean = true,
    val data: PlanData = PlanData()
)
