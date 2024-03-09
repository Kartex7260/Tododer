package kanti.tododer.ui.common

import kanti.tododer.ui.components.plan.PlanData
import javax.annotation.concurrent.Immutable

@Immutable
data class PlanUiState(
    override val selected: Boolean = false,
    override val visible: Boolean = true,
    val data: PlanData = PlanData()
) : Visible, Selectable
