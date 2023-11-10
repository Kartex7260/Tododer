package kanti.tododer.ui.fragments.components.common

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.model.plan.BasePlan

data class PlanProgressRequest(
	val plan: BasePlan,
	val callback: MutableLiveData<Float>
)
