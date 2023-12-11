package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.toTask

class FakeTaskDao(
	val tasks: MutableList<TaskEntity> = mutableListOf()
) : TaskDao {

	override suspend fun getAll(): List<TaskEntity> {
		return tasks.sortedBy { it.id }
	}

	override suspend fun getChildren(parentId: String): List<TaskEntity> {
		return tasks.filter { it.parentId == parentId }
	}

	override suspend fun getByRowId(rowId: Long): TaskEntity? {
		return getTask(rowId.toInt())
	}

	override suspend fun getTask(id: Int): TaskEntity? {
		return tasks.firstOrNull { it.id == id }
	}

	override suspend fun insert(vararg task: TaskEntity): Array<Long> {
		return Array(task.size) { index ->
			val tsk = if (task[index].id == 0) {
				val lastTaskId = tasks.lastOrNull()?.id ?: 0
				task[index].toTaskEntity(
					id = lastTaskId + 1
				)
			} else task[index]

			val rowId = tasks.indexOfFirst { it.id == tsk.id }
			if (rowId == -1) {
				tasks.add(tsk)
			} else {
				tasks[rowId] = tsk
			}
			tsk.id.toLong()
		}
	}

	override suspend fun delete(vararg task: TaskEntity): Int {
		var deletedCount = 0
		for (tsk in task) {
			if (tasks.removeIf { it.id == tsk.id })
				deletedCount++
		}
		return deletedCount
	}

	override suspend fun deleteAll() {
		tasks.clear()
	}
}