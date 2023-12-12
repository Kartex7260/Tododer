package kanti.tododer.domain.progress

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.progress.TodoProgress
import kanti.tododer.data.progress.TodoProgressRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ComputePlanProgressUseCase @Inject constructor(
	private val planProgressRepository: TodoProgressRepository,
	private val computeTodoProgressUseCase: ComputeTodoProgressUseCase,
) {

	private val defProgressValue: Float = 0f

	suspend operator fun invoke(
		plan: kanti.tododer.data.model.plan.Plan,
		callback: MutableSharedFlow<GetRepositoryResult<Float>>
	) {
		sendCachedData(plan, callback)
		val progress = computeTodoProgressUseCase(plan)
		callback.emit(
			value = repositoryResult(progress)
		)
	}

	private suspend fun sendCachedData(
		plan: kanti.tododer.data.model.plan.Plan,
		callback: MutableSharedFlow<GetRepositoryResult<Float>>
	) {
		val repRes = planProgressRepository.getPlanProgress(plan.fullId)
		val successRep = repRes.asSuccess
		callback.emit(
			value = repositoryResult(successRep?.value?.progress)
		)
	}

	private fun repositoryResult(progress: Float?): GetRepositoryResult<Float> {
		return GetRepositoryResult.Success(
			value = progress ?: defProgressValue
		)
	}

}