package kanti.tododer.domain.deletetodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeletePlanWithProgenyUseCase @Inject constructor() {

	suspend operator fun invoke(
		planRepository: PlanRepository,
		taskRepository: TaskRepository,
		id: Int
	): Boolean {
		val parentPlan = planRepository.getTodo(id)
		if (parentPlan.type is RepositoryResult.Type.NotFound || parentPlan.value == null)
			return false

		val result = planRepository.delete(parentPlan.value)
		deleteChildPlans(planRepository, taskRepository, parentPlan.value.fullId)

		return result
	}

	private suspend fun deleteChildPlans(
		planRepository: PlanRepository,
		taskRepository: TaskRepository,
		fullId: String
	) {
		val childPlans = planRepository.getChildren(fullId)
		val childTasks = taskRepository.getChildren(fullId)

		coroutineScope {
			launch {
				for (child in childPlans.value!!) {
					planRepository.delete(child)
					deleteChildPlans(planRepository, taskRepository, fullId)
				}
			}
			launch {
				for (child in childTasks.value!!) {
					taskRepository.delete(child)
					deleteChildTasks(taskRepository, child.fullId)
				}
			}
		}
	}

	private suspend fun deleteChildTasks(
		taskRepository: TaskRepository,
		fullId: String
	) {
		val children = taskRepository.getChildren(fullId)
		for (child in children.value!!) {
			taskRepository.delete(child)
			deleteChildTasks(taskRepository, fullId)
		}
	}

}