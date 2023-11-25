package kanti.tododer.domain.archiving

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.hollowparents.CreateHollowForerunnersUseCase
import kanti.tododer.domain.todomove.MoveTodoUseCase
import kanti.tododer.domain.todomove.RepositorySet
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArchiveTodoUseCase @Inject constructor(
	@StandardDataQualifier private val standardTaskRepository: TaskRepository,
	@StandardDataQualifier private val standardPlanRepository: PlanRepository,
	@ArchiveDataQualifier private val archiveTaskRepository: TaskRepository,
	@ArchiveDataQualifier private val archivePlanRepository: PlanRepository,
	private val moveTodoUseCase: MoveTodoUseCase,
	private val createHollowForerunnersUseCase: CreateHollowForerunnersUseCase
) {

	suspend operator fun invoke(todo: Todo): RepositoryResult<Unit> {
		coroutineScope {
			launch {
				createHollowForerunnersUseCase(todo)
			}
		}
		return moveTodoUseCase(
			from = RepositorySet(
				standardTaskRepository,
				standardPlanRepository
			),
			to = RepositorySet(
				archiveTaskRepository,
				archivePlanRepository
			),
			todo
		)
	}

}