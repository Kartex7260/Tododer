package kanti.tododer.domain.archiving

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.task.asTask
import kanti.tododer.domain.archiving.archive.ArchivePlanUseCase
import kanti.tododer.domain.archiving.archive.ArchiveTaskUseCase
import javax.inject.Inject

class ArchiveTodoUseCase @Inject constructor(
	private val archiveTaskUseCase: ArchiveTaskUseCase,
	private val archivePlanUseCase: ArchivePlanUseCase
) {

	suspend operator fun invoke(todo: Todo) {
		when (todo.type) {
			Todo.Type.TASK -> archiveTaskUseCase(todo.asTask)
			Todo.Type.PLAN -> archivePlanUseCase(todo.asPlan)
		}
	}

}