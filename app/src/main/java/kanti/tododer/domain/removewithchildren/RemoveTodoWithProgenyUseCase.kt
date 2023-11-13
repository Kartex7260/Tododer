package kanti.tododer.domain.removewithchildren

import kanti.tododer.data.model.common.Todo
import javax.inject.Inject

class RemoveTodoWithProgenyUseCase @Inject constructor(
	private val removePlanWithProgenyUseCase: RemovePlanWithProgenyUseCase,
	private val removeTaskWithProgenyUseCase: RemoveTaskWithProgenyUseCase
) {

	suspend operator fun invoke(todo: Todo) {
		when (todo.type) {
			Todo.Type.PLAN -> removePlanWithProgenyUseCase(todo.id)
			Todo.Type.TASK -> removeTaskWithProgenyUseCase(todo.id)
		}
	}

}