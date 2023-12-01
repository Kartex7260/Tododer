package kanti.tododer.domain.removewithchildren

import kanti.tododer.data.model.common.Todo
import javax.inject.Inject

class RemoveTodoWithChildrenUseCase @Inject constructor(
	private val removePlanWithChildrenUseCase: RemovePlanWithChildrenUseCase,
	private val removeTaskWithChildrenUseCase: RemoveTaskWithChildrenUseCase
) {

	suspend operator fun invoke(todo: Todo): Boolean {
		return when (todo.type) {
			Todo.Type.PLAN -> removePlanWithChildrenUseCase(todo.id)
			Todo.Type.TASK -> removeTaskWithChildrenUseCase(todo.id)
		}
	}

}