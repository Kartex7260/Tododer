package kanti.tododer.ui.fragments.components.common

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.plan.Plan
import kotlinx.coroutines.flow.MutableSharedFlow

data class PlanProgressRequest(
	val plan: Plan,
	val callback: MutableSharedFlow<GetRepositoryResult<Float>>
)
