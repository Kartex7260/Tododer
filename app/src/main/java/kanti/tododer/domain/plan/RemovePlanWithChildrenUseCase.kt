package kanti.tododer.domain.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.fullId
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.fullId
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemovePlanWithChildrenUseCase @Inject constructor(
	private val planRepository: IPlanRepository,
	private val taskRepository: ITaskRepository
) {

	suspend operator fun invoke(id: Int): Boolean {
		val parentPlan = planRepository.getPlan(id)
		if (parentPlan.type is RepositoryResult.Type.NotFound || parentPlan.value == null)
			return false

		val result = planRepository.delete(parentPlan.value)
		deleteChildPlans(parentPlan.value.fullId)

		return result
	}

	private suspend fun deleteChildPlans(fullId: String) {
		val childPlans = planRepository.getChildren(fullId)
		val childTasks = taskRepository.getChildren(fullId)

		coroutineScope {
			launch {
				for (child in childPlans.value!!) {
					planRepository.delete(child)
					deleteChildPlans(child.fullId)
				}
			}
			launch {
				for (child in childTasks.value!!) {
					taskRepository.delete(child)
					deleteChildTasks(child.fullId)
				}
			}
		}
	}

	private suspend fun deleteChildTasks(fullId: String) {
		val children = taskRepository.getChildren(fullId)
		for (child in children.value!!) {
			taskRepository.delete(child)
			deleteChildTasks(child.fullId)
		}
	}

}