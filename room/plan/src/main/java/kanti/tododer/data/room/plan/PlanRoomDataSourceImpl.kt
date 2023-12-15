package kanti.tododer.data.room.plan

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.model.plan.toPlan
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import javax.inject.Inject

class PlanRoomDataSourceImpl @Inject constructor(
	private val planDao: PlanDao,
	private val planDaoFiller: PlanDaoFiller
) : PlanLocalDataSource {

	override val standardPlans: Flow<List<Plan>>
		get() = planDao.getAll(false)
	override val archivedPlans: Flow<List<Plan>>
		get() = planDao.getAll(true)

	override suspend fun insert(plan: Plan): Plan {
		val rowId = planDao.insert(plan.toPlanEntity())
		if (rowId == -1L)
			throw IllegalArgumentException("Plan already exist (id = ${plan.id})")
		return planDao.getByRowId(rowId)?.toPlan()
			?: throw IllegalStateException("Not found plan by row id (rowId = $rowId)")
	}

	override suspend fun update(plans: List<Plan>) {
		planDao.update(
			plans.map {
				it.toPlanEntity()
			}
		)
	}

	override suspend fun update(plan: Plan): Plan {
		val id = plan.id
		planDao.update(listOf(plan.toPlanEntity()))
		return planDao.getPlan(id)?.toPlan()
			?: throw IllegalStateException("Not found plan by id ($id)")
	}

	override suspend fun delete(plans: List<Plan>) {
		planDao.delete(
			plans.map {
				it.toPlanEntity()
			}
		)
	}

	override suspend fun init() {
		if (isEmpty())
			planDaoFiller.fill(planDao)
	}

	override suspend fun isEmpty(): Boolean {
		return planDao.count() == 0
	}

	override suspend fun clear() {
		planDao.deleteAll()
		init()
	}
}