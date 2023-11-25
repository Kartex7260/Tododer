package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.datasource.local.DefaultFakeRoomDao

class FakePlanDao(
	val plans: MutableList<PlanEntity> = mutableListOf()
) : PlanDao {

	private val baseDao = DefaultFakeRoomDao(
		newId = { newId ->
			toPlanEntity(
				id = newId
			)
		},
		todos = plans
	)

	override suspend fun getChildrenRoom(parentId: String): List<PlanEntity> {
		return baseDao.getChildrenRoom(parentId)
	}

	override suspend fun getByRowIdRoom(rowId: Long): PlanEntity? {
		return baseDao.getByRowIdRoom(rowId)
	}

	override suspend fun getPlanRoom(id: Int): PlanEntity? {
		return baseDao.getTodoRoom(id)
	}

	override suspend fun insertRoom(vararg plan: PlanEntity): Array<Long> {
		return baseDao.insertRoom(*plan)
	}

	override suspend fun updateRoom(vararg plan: PlanEntity): Int {
		return baseDao.updateRoom(*plan)
	}

	override suspend fun deleteRoom(vararg plan: PlanEntity): Int {
		return baseDao.deleteRoom(*plan)
	}

	override suspend fun deleteAllRoom() {
		baseDao.deleteAllRoom()
	}
}