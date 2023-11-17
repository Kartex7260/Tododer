package kanti.tododer.common.features

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kotlinx.coroutines.launch

interface ComputePlanProgressFeature : CoroutineScopeFeature, RepositorySetFeature,
	TodoProgressRepositoryFeature {

	val computePlanProgressUseCase: ComputePlanProgressUseCase

	fun computePlanProgress(plan: BasePlan, callback: MutableLiveData<Float>) {
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