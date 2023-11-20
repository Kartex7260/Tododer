package kanti.tododer.domain.gettodowithprogeny.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskRepository
import javax.inject.Inject

class GetTaskWithProgenyUseCase @Inject constructor() {

	suspend operator fun invoke(
		taskRepository: TaskRepository,
		todo: Todo
	): RepositoryResult<TaskWithProgeny> {
		val task = taskRepository.getTask(todo.id).value
			?: return RepositoryResult(type = RepositoryResult.Type.NotFound(todo.fullId))

		val tasks = mutableListOf<Task>()
		tasks.add(task)
		getChildrenToList(taskRepository, task, tasks)
		return RepositoryResult(
			value = TaskWithProgeny(tasks)
		)
	}

	private suspend fun getChildrenToList(
		taskRepository: TaskRepository,
		todo: Todo,
		list: MutableList<Task>
	) {
		val children = taskRepository.getChildren(todo.fullId).value
			?: return

		for (child in children) {
			list.add(child)
			getChildrenToList(taskRepository, child, list)
		}
	}

}