package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.room.plan.PlanDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	private val planDao: PlanDao,
	private val initializer: PlanInitializer
) : PlanLocalDataSource {

	override val standardPlans: Flow<List<Plan>>
		get() = planDao.getAll(false).map { plans -> plans.map { it.toPlan() } }
	override val archivedPlans: Flow<List<Plan>>
		get() = planDao.getAll(true).map { plans -> plans.map { it.toPlan() } }

	override suspend fun insert(plan: Plan): Plan {
		val rowId = planDao.insert(plan.toPlanEntity())
		if (rowId == -1L)
			throw IllegalArgumentException("Plan(id = ${plan.id}) already exist!")
		return planDao.getByRowId(rowId)?.toPlan()
			?: throw IllegalStateException("Not found plan by rowId=$rowId")
	}

	override suspend fun update(plan: Plan): Plan {
		val id = plan.id
		planDao.update(listOf(plan.toPlanEntity()))
		return planDao.getPlan(id)?.toPlan()
			?:throw IllegalStateException("Not found plan by id=$id")
	}

	override suspend fun update(plans: List<Plan>) {
		planDao.update(plans.map { it.toPlanEntity() })
	}

	override suspend fun delete(plans: List<Plan>) {
		planDao.delete(plans.map { it.toPlanEntity() })
	}

	override suspend fun init() {
		if (isEmpty())
			initializer.initialize(this)
	}

	override suspend fun isEmpty(): Boolean {
		return planDao.count() == 0
	}

	override suspend fun clear() {
		planDao.deleteAll()
		initializer.initialize(this)
	}
}