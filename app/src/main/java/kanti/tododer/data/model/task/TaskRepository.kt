package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import javax.inject.Inject

class TaskRepository @Inject constructor(
	private val taskLocal: TaskLocalDataSource
) {

	suspend fun getTask(id: Int): RepositoryResult<Task> {
		val task = taskLocal.getTask(id)
		return task.toRepositoryResult()
	}

	suspend fun getChildren(fid: String): RepositoryResult<List<Task>> {
		return taskLocal.getChildren(fid).toRepositoryResult()
	}

	suspend fun insert(task: Task): Task = taskLocal.insert(task)

	suspend fun replace(task: Task, body: (Task.() -> Task)? = null): Task {
		val newTask = body?.let { task.it() }
		return taskLocal.replace(newTask ?: task)
	}

	suspend fun delete(task: Task): Boolean = taskLocal.delete(task)

}