package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.datasource.local.TaskDao
import kanti.tododer.data.model.task.datasource.local.toTaskEntity

class FakeTaskDao(
	val tasks: MutableList<kanti.tododer.data.model.task.datasource.local.TaskEntity> = mutableListOf()
) : kanti.tododer.data.model.task.datasource.local.TaskDao {

	override suspend fun getAll(): List<kanti.tododer.data.model.task.datasource.local.TaskEntity> {
		return tasks.sortedBy { it.id }
	}

	override suspend fun getChildren(parentId: String): List<kanti.tododer.data.model.task.datasource.local.TaskEntity> {
		return tasks.filter { it.parentId == parentId }
	}

	override suspend fun getByRowId(rowId: Long): kanti.tododer.data.model.task.datasource.local.TaskEntity? {
		return getTask(rowId.toInt())
	}

	override suspend fun getTask(id: Int): kanti.tododer.data.model.task.datasource.local.TaskEntity? {
		return tasks.firstOrNull { it.id == id }
	}

	override suspend fun insert(vararg task: kanti.tododer.data.model.task.datasource.local.TaskEntity): Array<Long> {
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

	override suspend fun delete(vararg task: kanti.tododer.data.model.task.datasource.local.TaskEntity): Int {
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