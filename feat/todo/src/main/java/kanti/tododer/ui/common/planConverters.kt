package kanti.tododer.ui.common

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.components.plan.PlanData

fun Plan.toUiState(
    selected: Boolean = false,
    visible: Boolean = true,
    progress: Float = 0.0f
): PlanUiState = PlanUiState(
    selected = selected,
    visible = visible,
    data = toData(progress = progress)
)

fun Plan.toData(progress: Float = 0.0f): PlanData = PlanData(
    id = id,
    title = title,
    progress = progress
)