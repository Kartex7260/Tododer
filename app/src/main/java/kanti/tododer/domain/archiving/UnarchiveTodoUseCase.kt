package kanti.tododer.domain.archiving

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.todomove.MoveTodoUseCase
import kanti.tododer.data.model.RepositorySet
import javax.inject.Inject

class UnarchiveTodoUseCase @Inject constructor(
	@StandardDataQualifier private val standardTaskRepository: TaskRepository,
	@StandardDataQualifier private val standardPlanRepository: PlanRepository,
	@ArchiveDataQualifier private val archiveTaskRepository: TaskRepository,
	@ArchiveDataQualifier private val archivePlanRepository: PlanRepository,
	private val moveTodoUseCase: MoveTodoUseCase
) {

	suspend operator fun invoke(todo: Todo): RepositoryResult<Unit> {
		return moveTodoUseCase(
			from = RepositorySet(
				archiveTaskRepository,
				archivePlanRepository
			),
			to = RepositorySet(
				standardTaskRepository,
				standardPlanRepository
			),
			todo
		)
	}

}