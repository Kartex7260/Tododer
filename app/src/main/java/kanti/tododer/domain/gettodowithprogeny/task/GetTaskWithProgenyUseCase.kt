package kanti.tododer.domain.gettodowithprogeny.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.TaskRepository

class GetTaskWithProgenyUseCase(
	private val taskRepository: TaskRepository
) {

	suspend operator fun invoke(todo: Todo): RepositoryResult<TaskWithProgeny> {
		val task = taskRepository.getTask(todo.id).value
			?: return RepositoryResult(type = RepositoryResult.Type.NotFound(todo.fullId))

		val tasks = mutableListOf<BaseTask>()
		tasks.add(task)
		getChildrenToList(task, tasks)
		return RepositoryResult(
			value = TaskWithProgeny(tasks)
		)
	}

	private suspend fun getChildrenToList(todo: Todo, list: MutableList<BaseTask>) {
		val children = taskRepository.getChildren(todo.fullId).value
			?: return

		for (child in children) {
			list.add(child)
			getChildrenToList(child, list)
		}
	}

}