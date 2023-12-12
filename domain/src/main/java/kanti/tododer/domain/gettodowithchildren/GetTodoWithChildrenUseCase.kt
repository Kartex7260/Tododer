package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.fullid.FullId
import kanti.tododer.domain.common.TodoWithChildren
import javax.inject.Inject

class GetTodoWithChildrenUseCase @Inject constructor(
	private val getPlanWithChildrenUseCase: GetPlanWithChildrenUseCase,
	private val getTaskWithChildrenUseCase: GetPlanWithChildrenUseCase
) {

	suspend operator fun invoke(fullId: FullId): GetRepositoryResult<TodoWithChildren> {
		return when (fullId.type) {
			kanti.tododer.data.model.common.Todo.Type.PLAN -> getPlanWithChildrenUseCase(fullId.id)
			kanti.tododer.data.model.common.Todo.Type.TASK -> getTaskWithChildrenUseCase(fullId.id)
		}
	}

}