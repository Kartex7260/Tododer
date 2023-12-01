package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.GetRepositoryResult
import javax.inject.Inject

class GetTodoChildrenUseCase @Inject constructor(
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase,
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(todo: Todo): Result<List<Todo>> {
		return when(todo.type) {
			Todo.Type.TASK -> getTaskChildrenUseCase(todo)
			Todo.Type.PLAN -> getPlanChildrenUseCase(todo)
		}
	}

}