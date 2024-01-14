package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeletePlan @Inject constructor(
	private val todoRepository: TodoRepository,
	private val planRepository: PlanRepository
) {

	suspend operator fun invoke(planIds: List<Long>) {
		coroutineScope {
			launch {
				planRepository.delete(planIds)
			}

			for (planId in planIds) {
				todoRepository.deleteChildren(
					fullId = FullId(planId, FullIdType.Plan)
				)
			}
		}
	}
}