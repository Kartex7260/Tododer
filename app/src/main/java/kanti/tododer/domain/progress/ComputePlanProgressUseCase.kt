package kanti.tododer.domain.progress

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class ComputePlanProgressUseCase @Inject constructor(
	@StandardDataQualifier private val todoProgressRepository: TodoProgressRepository,
	private val computeTodoProgressUseCase: ComputeTodoProgressUseCase,
) {

	private val defProgressValue: Float = 0f

	suspend operator fun invoke(plan: BasePlan, callback: MutableLiveData<Float>) {
		sendCachedData(plan, callback)
		val progress = computeTodoProgressUseCase(plan)
		callback.postValue(progress)
	}

	private suspend fun sendCachedData(plan: BasePlan, callback: MutableLiveData<Float>) {
		val repRes = todoProgressRepository.getTodoProgress(plan.fullId)
		when (repRes.type) {
			RepositoryResult.Type.SuccessLocal -> {
				callback.postValue(repRes.value?.progress ?: defProgressValue)
			}
			else -> {
				callback.postValue(defProgressValue)
			}
		}
	}

}