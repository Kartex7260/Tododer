package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.Todo
import javax.inject.Inject

class GetTodoChildrenUseCase @Inject constructor(
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase,
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(todo: Todo): List<Todo> = when(todo.type) {
		Todo.Type.TASK -> getTaskChildrenUseCase(todo)
		Todo.Type.PLAN -> getPlanChildrenUseCase(todo)
	}

}