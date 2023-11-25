package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.common.datasource.local.DefaultFakeRoomDao

class FakeTaskDao(
	val tasks: MutableList<TaskEntity> = mutableListOf()
) : TaskDao {

	private val baseDao = DefaultFakeRoomDao(
		newId = { newId ->
			toTaskEntity(
				id = newId
			)
		},
		todos = tasks
	)

	override suspend fun getChildrenRoom(parentId: String): List<TaskEntity> {
		return baseDao.getChildrenRoom(parentId)
	}

	override suspend fun getByRowIdRoom(rowId: Long): TaskEntity? {
		return baseDao.getByRowIdRoom(rowId)
	}

	override suspend fun getTaskRoom(id: Int): TaskEntity? {
		return baseDao.getTodoRoom(id)
	}

	override suspend fun insertRoom(vararg task: TaskEntity): Array<Long> {
		return baseDao.insertRoom(*task)
	}

	override suspend fun updateRoom(vararg task: TaskEntity): Int {
		return baseDao.updateRoom(*task)
	}

	override suspend fun deleteRoom(vararg task: TaskEntity): Int {
		return baseDao.deleteRoom(*task)
	}

	override suspend fun deleteAllRoom() {
		baseDao.deleteAllRoom()
	}
}