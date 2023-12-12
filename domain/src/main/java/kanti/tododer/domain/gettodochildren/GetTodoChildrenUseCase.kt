package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import javax.inject.Inject

class GetTodoChildrenUseCase @Inject constructor(
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase,
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(todo: Todo): Result<List<Todo>> {
		return when(todo.type) {
			kanti.tododer.data.model.common.Todo.Type.TASK -> getTaskChildrenUseCase(todo)
			kanti.tododer.data.model.common.Todo.Type.PLAN -> getPlanChildrenUseCase(todo)
		}
	}

}