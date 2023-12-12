package kanti.tododer.domain.removewithchildren

import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemovePlanWithChildrenUseCase @Inject constructor(
	private val planRepository: PlanRepository,
	private val taskRepository: TaskRepository
) {

	suspend operator fun invoke(id: Int): Boolean {
		val parentPlan = planRepository.getPlan(id)
		val successParentPlan = parentPlan.asSuccess ?: return false

		val result = planRepository.delete(successParentPlan.value)
		deleteChildPlans(successParentPlan.value.fullId)

		return result.isSuccess
	}

	private suspend fun deleteChildPlans(fullId: String) {
		val childPlans = planRepository.getChildren(fullId)
		val childTasks = taskRepository.getChildren(fullId)

		val sucChildPlans = childPlans.getOrDefault(listOf())
		val sucChildTasks = childTasks.getOrDefault(listOf())

		coroutineScope {
			launch {
				for (child in sucChildPlans) {
					planRepository.delete(child)
					deleteChildPlans(child.fullId)
				}
			}
			launch {
				for (child in sucChildTasks) {
					taskRepository.delete(child)
					deleteChildTasks(child.fullId)
				}
			}
		}
	}

	private suspend fun deleteChildTasks(fullId: String) {
		val children = taskRepository.getChildren(fullId)
		val sucChildren = children.getOrDefault(listOf())
		for (child in sucChildren) {
			taskRepository.delete(child)
			deleteChildTasks(child.fullId)
		}
	}

}