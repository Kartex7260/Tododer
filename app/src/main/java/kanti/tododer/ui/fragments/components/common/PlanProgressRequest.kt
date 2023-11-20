package kanti.tododer.ui.fragments.components.common

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.model.plan.Plan

data class PlanProgressRequest(
	val plan: Plan,
	val callback: MutableLiveData<Float>
)
