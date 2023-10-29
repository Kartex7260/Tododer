package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.common.TodoWithChildren
import javax.inject.Inject

class GetTodoWithChildrenUseCase @Inject constructor(
	private val getPlanWithChildrenUseCase: GetPlanWithChildrenUseCase,
	private val getTaskWithChildrenUseCase: GetPlanWithChildrenUseCase
) {

	suspend operator fun invoke(fullId: FullId): RepositoryResult<TodoWithChildren> {
		return when (fullId.type) {
			Todo.Type.PLAN -> getPlanWithChildrenUseCase(fullId.id)
			Todo.Type.TASK -> getTaskWithChildrenUseCase(fullId.id)
		}
	}

}