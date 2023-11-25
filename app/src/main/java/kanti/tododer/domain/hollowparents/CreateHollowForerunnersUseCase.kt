package kanti.tododer.domain.hollowparents

import kanti.tododer.data.model.common.ParentOwner
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.archive.toArchivePlan
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.archive.toArchiveTask
import kanti.tododer.data.model.task.asTask
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.GetForerunnersUseCase
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class CreateHollowForerunnersUseCase @Inject constructor(
	private val getForerunnersUseCase: GetForerunnersUseCase,
	@StandardDataQualifier private val taskRepository: TaskRepository,
	@StandardDataQualifier private val planRepository: PlanRepository,
	@ArchiveDataQualifier private val archiveTaskRepository: TaskRepository,
	@ArchiveDataQualifier private val archivePlanRepository: PlanRepository
) {

	suspend operator fun invoke(
		parentOwner: ParentOwner
	) {
		val forerunners = getForerunnersUseCase(
			repositorySet = RepositorySet(
				taskRepository, planRepository
			),
			parentOwner = parentOwner
		)
		for (parent in forerunners) {
			addHollowParent(parent)
		}
	}

	private suspend fun addHollowParent(todo: Todo) {
		when (todo.type) {
			Todo.Type.PLAN -> {
				archivePlanRepository.update(todo.asPlan) {
					toArchivePlan(
						hollow = true
					)
				}
			}
			Todo.Type.TASK -> {
				archiveTaskRepository.update(todo.asTask) {
					toArchiveTask(
						hollow = true
					)
				}
			}
		}
	}

}