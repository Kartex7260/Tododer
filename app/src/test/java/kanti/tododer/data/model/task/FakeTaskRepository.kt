package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult

class FakeTaskRepository : TaskRepository {

	private val _tasks = mutableListOf<Task>()
	val tasks: List<Task>
		get() = _tasks.toList()

	override suspend fun getTask(id: Int): RepositoryResult<Task> {
		val task = _tasks.firstOrNull { it.id == id }
			?: return RepositoryResult(type = RepositoryResult.Type.NotFound("Id: $id"))
		return RepositoryResult(task)
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<Task>> {
		val children = _tasks.filter { it.parentId == fid }
		return RepositoryResult(children)
	}

	override suspend fun insert(task: Task): RepositoryResult<Task> {
		val taskWithId = taskWithId(task)
		val index = _tasks.indexOfFirst { it.id == task.id }
		if (index == -1) {
			_tasks.add(taskWithId)
		} else {
			return RepositoryResult(type = RepositoryResult.Type.AlreadyExists(taskWithId.fullId))
		}
		return RepositoryResult(taskWithId)
	}

	override suspend fun replace(task: Task, body: (Task.() -> Task)?): RepositoryResult<Task> {
		val editedTask = body?.let { task.it() } ?: task
		val taskWithId = taskWithId(editedTask)
		val index = _tasks.indexOfFirst { it.id == task.id }
		if (index == -1) {
			_tasks.add(taskWithId)
		} else {
			_tasks[index] = taskWithId
		}
		return RepositoryResult(taskWithId)
	}

	override suspend fun delete(task: Task): Boolean {
		return _tasks.removeIf { it.id == task.id }
	}

	override suspend fun deleteAll() {
		_tasks.clear()
	}

	val isEmpty: Boolean
		get() = _tasks.isEmpty()

	private fun taskWithId(task: Task): Task {
		return if (task.id != 0) {
			task
		} else {
			task.copy(id = _tasks.size + 1)
		}
	}

}