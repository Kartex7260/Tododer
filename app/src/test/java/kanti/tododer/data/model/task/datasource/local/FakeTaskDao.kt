package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.Task

class FakeTaskDao : TaskDao() {

	private val tasks = mutableListOf<TaskEntity>()

	override suspend fun getChildren(parentId: String): List<TaskEntity> {
		return tasks.filter { it.parentId == parentId }
	}

	override suspend fun getByRowId(rowId: Long): TaskEntity? {
		if (rowId < 0)
			return null
		return tasks[rowId.toInt()]
	}

	override suspend fun getTask(id: Int): TaskEntity? {
		return tasks.firstOrNull { it.id == id }
	}

	override suspend fun replace(task: TaskEntity): Long {
		val taskWithId = taskWithId(task)
		fun rowId() = tasks.size.toLong() - 1
		if (task.id == 0) {
			tasks.add(taskWithId)
			return rowId()
		} else {
			val rowId = tasks.indexOfFirst { it.id == taskWithId.id }
			if (rowId == -1) {
				tasks.add(taskWithId)
				return rowId()
			}
			tasks[rowId] = taskWithId
			return tasks.indexOfFirst { it.id == task.id }.toLong()
		}
	}

	override suspend fun insert(task: TaskEntity): Long {
		if (tasks.firstOrNull { it.id == task.id } != null)
			return -1
		val taskWithId = taskWithId(task)
		fun rowId() = tasks.size.toLong() - 1
		if (task.id == 0) {
			tasks.add(taskWithId)
			return rowId()
		} else {
			val rowId = tasks.indexOfFirst { it.id == taskWithId.id }
			if (rowId == -1) {
				tasks.add(taskWithId)
				return rowId()
			}
			return -1
		}
	}

	override suspend fun delete(task: TaskEntity): Int {
		return if (tasks.removeIf { it.id == task.id }) 1 else 0
	}

	override suspend fun deleteAll() {
		tasks.clear()
	}

	private fun taskWithId(task: TaskEntity): TaskEntity {
		return if (task.id != 0) {
			task
		} else {
			task.copy(id = tasks.size + 1)
		}
	}

	fun clear() {
		tasks.clear()
	}

	val isEmpty: Boolean
		get() {
			return tasks.isEmpty()
		}

}