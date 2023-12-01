package kanti.tododer.domain.progress

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.progress.ITodoProgressRepository
import kanti.tododer.domain.gettodochildren.GetTodoChildrenUseCase
import javax.inject.Inject

class ComputePlanProgressUseCase @Inject constructor(
	private val planProgressRepository: ITodoProgressRepository,
	private val computeTodoProgressUseCase: ComputeTodoProgressUseCase,
) {

	private val defProgressValue: Float = 0f

	suspend operator fun invoke(plan: Plan, callback: MutableLiveData<Float>) {
		sendCachedData(plan, callback)
		val progress = computeTodoProgressUseCase(plan)
		callback.postValue(progress)
	}

	private suspend fun sendCachedData(plan: Plan, callback: MutableLiveData<Float>) {
		val repRes = planProgressRepository.getPlanProgress(plan.fullId)
		when (repRes.type) {
			RepositoryResult.Type.Success -> {
				callback.postValue(repRes.value?.progress ?: defProgressValue)
			}
			else -> {
				callback.postValue(defProgressValue)
			}
		}
	}

}