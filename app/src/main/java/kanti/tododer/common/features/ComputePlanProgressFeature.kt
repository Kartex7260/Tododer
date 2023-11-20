package kanti.tododer.common.features

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kotlinx.coroutines.launch

interface ComputePlanProgressFeature : CoroutineScopeFeature, RepositorySetFeature,
	TodoProgressRepositoryFeature {

	val computePlanProgressUseCase: ComputePlanProgressUseCase

	fun computePlanProgress(plan: Plan, callback: MutableLiveData<Float>) {
		coroutineScope.launch {
			computePlanProgressUseCase(
				todoProgressRepository,
				repositorySet,
				plan,
				callback
			)
		}
	}

}