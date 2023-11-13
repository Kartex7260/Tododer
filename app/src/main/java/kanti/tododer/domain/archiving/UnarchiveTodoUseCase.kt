package kanti.tododer.domain.archiving

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.task.asTask
import kanti.tododer.domain.archiving.unarchive.UnarchivePlanUseCase
import kanti.tododer.domain.archiving.unarchive.UnarchiveTaskUseCase
import javax.inject.Inject

class UnarchiveTodoUseCase @Inject constructor(
	private val unarchiveTaskUseCase: UnarchiveTaskUseCase,
	private val unarchivePlanUseCase: UnarchivePlanUseCase
) {

	suspend operator fun invoke(todo: Todo) {
		when (todo.type) {
			Todo.Type.TASK -> unarchiveTaskUseCase(todo.asTask)
			Todo.Type.PLAN -> unarchivePlanUseCase(todo.asPlan)
		}
	}

}