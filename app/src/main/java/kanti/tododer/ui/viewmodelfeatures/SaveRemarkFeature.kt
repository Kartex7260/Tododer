package kanti.tododer.ui.viewmodelfeatures

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.data.model.task.toTask
import kotlinx.coroutines.launch

interface SaveRemarkFeature : CoroutineScopeFeature, UpdatePlanFeature, UpdateTaskFeature {

	fun saveRemark(todo: Todo, remark: String) {
		coroutineScope.launch {
			val fullId = todo.toFullId
			when (fullId.type) {
				Todo.Type.TASK -> updateTask(fullId.id) {
					toTask(
						remark = remark
					)
				}
				Todo.Type.PLAN -> updatePlan(fullId.id) {
					toPlan(
						remark = remark
					)
				}
			}
		}
	}

}