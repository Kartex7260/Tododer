package kanti.tododer.domain.removewithchildren

import kanti.tododer.data.model.common.Todo
import javax.inject.Inject

class RemoveTodoWithChildrenUseCase @Inject constructor(
	private val removePlanWithChildrenUseCase: RemovePlanWithChildrenUseCase,
	private val removeTaskWithChildrenUseCase: RemoveTaskWithChildrenUseCase
) {

	suspend operator fun invoke(todo: Todo): Boolean {
		return when (todo.type) {
			kanti.tododer.data.model.common.Todo.Type.PLAN -> removePlanWithChildrenUseCase(todo.id)
			kanti.tododer.data.model.common.Todo.Type.TASK -> removeTaskWithChildrenUseCase(todo.id)
		}
	}

}