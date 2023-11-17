package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetTodoChildrenUseCase @Inject constructor(
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase,
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		todo: Todo
	): List<Todo> = when(todo.type) {
		Todo.Type.TASK -> getTaskChildrenUseCase(
			repositorySet.taskRepository,
			todo
		)
		Todo.Type.PLAN -> getPlanChildrenUseCase(
			repositorySet,
			todo
		)
	}

}