package kanti.tododer.common.features

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toFullId
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.data.model.task.toTask
import kotlinx.coroutines.launch

interface SaveTitleFeature : CoroutineScopeFeature, UpdateTaskFeature, UpdatePlanFeature {

	fun saveTitle(todo: Todo, title: String) {
		coroutineScope.launch {
			val fullId = todo.toFullId
			when (fullId.type) {
				Todo.Type.TASK -> updateTask(fullId.id) {
					toTask(
						title = title
					)
				}
				Todo.Type.PLAN -> updatePlan(fullId.id) {
					toPlan(
						title = title
					)
				}
			}
		}
	}

}