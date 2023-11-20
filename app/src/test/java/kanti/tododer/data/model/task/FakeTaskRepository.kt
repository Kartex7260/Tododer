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

	override suspend fun insert(vararg task: Task): RepositoryResult<Unit> {
		val taskWithId = tasksWithId(*task)
		return try {
			_tasks.addAll(taskWithId)
			RepositoryResult()
		} catch (th: Throwable) {
			RepositoryResult(type = RepositoryResult.Type.Fail(th.message))
		}
	}

	override suspend fun update(task: Task, update: (Task.() -> Task)?): RepositoryResult<Task> {
		val editedTask = update?.let { task.it() } ?: task
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

	private class LastIndexOwner(var lastIndex: Int)

	private fun tasksWithId(vararg task: Task): Array<Task> {
		var lastIndex = _tasks.size
		return Array(task.size) { index ->
			if (task[index].id != 0) {
				task[index]
			} else {
				task[index].toTask(id = lastIndex++ + 1)
			}
		}
	}

	private fun taskWithId(task: Task): Task {
		return if (task.id != 0) {
			task
		} else {
			task.toTask(id = _tasks.size + 1)
		}
	}

}