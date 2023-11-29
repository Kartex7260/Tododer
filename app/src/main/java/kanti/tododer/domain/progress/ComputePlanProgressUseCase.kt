package kanti.tododer.domain.progress

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.data.model.RepositorySet
import javax.inject.Inject

class ComputePlanProgressUseCase @Inject constructor(
	private val computeTodoProgressUseCase: ComputeTodoProgressUseCase,
) {

	private val defProgressValue: Float = 0f

	suspend operator fun invoke(
		todoProgressRepository: TodoProgressRepository,
		repositorySet: RepositorySet,
		plan: Plan,
		callback: MutableLiveData<Float>
	) {
		sendCachedData(
			todoProgressRepository,
			plan,
			callback
		)
		val progress = computeTodoProgressUseCase(
			todoProgressRepository,
			repositorySet,
			plan
		)
		callback.postValue(progress)
	}

	private suspend fun sendCachedData(
		todoProgressRepository: TodoProgressRepository,
		plan: Plan,
		callback: MutableLiveData<Float>
	) {
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