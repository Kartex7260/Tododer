package kanti.tododer.domain.todomove

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.domain.gettodowithprogeny.task.GetTaskWithProgenyUseCase
import kanti.tododer.domain.gettodowithprogeny.task.TaskWithProgeny
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoveTaskUseCase @Inject constructor(
	private val getTaskWithProgenyUseCase: GetTaskWithProgenyUseCase
) {

	suspend operator fun invoke(
		from: TaskRepository,
		to: TaskRepository,
		task: BaseTask
	): RepositoryResult<Unit> {
		val taskWithProgeny = getTaskWithProgenyUseCase(from, task)
		if (!taskWithProgeny.isSuccess || taskWithProgeny.value == null)
			return RepositoryResult(type = taskWithProgeny.type)

		return coroutineScope {
			val removeJob = launch { removeFrom(from, taskWithProgeny.value) }
			val addJob = launch { addTo(to, taskWithProgeny.value) }
			listOf(removeJob, addJob).joinAll()
			RepositoryResult()
		}
	}

	private suspend fun removeFrom(from: TaskRepository, taskWithProgeny: TaskWithProgeny) {
		from.delete(*taskWithProgeny.tasks.toTypedArray())
	}

	private suspend fun addTo(to: TaskRepository, taskWithProgeny: TaskWithProgeny) {
		to.update(*taskWithProgeny.tasks.toTypedArray())
	}

}