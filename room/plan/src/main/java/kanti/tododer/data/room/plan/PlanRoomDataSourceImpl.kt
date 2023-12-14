package kanti.tododer.data.room.plan

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanRoomDataSourceImpl @Inject constructor(
	private val planDao: PlanDao,
	private val planDaoFiller: PlanDaoFiller
) : PlanLocalDataSource {

	override val standardPlans: Flow<List<Plan>>
		get() = planDao.getAll(false)
	override val archivedPlans: Flow<List<Plan>>
		get() = planDao.getAll(true)

	override suspend fun insert(vararg plan: Plan) {
		planDao.insert(*plan.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun update(vararg plan: Plan) {
		planDao.update(*plan.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun delete(vararg plan: Plan) {
		planDao.delete(*plan.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun isEmpty(): Boolean {
		return planDao.count() == 0
	}

	override suspend fun clear() {
		planDao.deleteAll()
		planDaoFiller.fill(planDao)
	}
}