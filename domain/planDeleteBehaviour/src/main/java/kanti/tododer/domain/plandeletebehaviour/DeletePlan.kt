package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeletePlan @Inject constructor(
	private val todoRepository: TodoRepository,
	private val planRepository: PlanRepository,
	private val appDataRepository: AppDataRepository,
	@StandardLog private val logger: Logger
) {

	suspend operator fun invoke(planIds: List<Long>) {
		if (planIds.isEmpty()) {
			logger.d(LOG_TAG, "invoke(List<Long> = count(0)): don`t invoke")
			return
		}
		coroutineScope {
			launch {
				planRepository.delete(planIds)
			}

			for (planId in planIds) {
				todoRepository.deleteChildren(
					fullId = FullId(planId, FullIdType.Plan)
				)
				launch {
					appDataRepository.deleteIfCurrent(planId)
				}
			}
		}
		logger.d(LOG_TAG, "invoke(List<Long> = count(${planIds.size}))")
	}

	companion object {

		private const val LOG_TAG = "DeletePlan"
	}
}