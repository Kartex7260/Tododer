package kanti.tododer.data.model.plan.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanState
import kanti.tododer.data.room.plan.PlanDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	private val planDao: PlanDao,
	private val initializer: PlanInitializer,
	private val sl: StateLanguage
) : PlanLocalDataSource {

	override val standardPlans: Flow<List<Plan>>
		get() {
			return planDao.getAll(PlanState.Normal.name).map {
				plans -> plans.map {
					it.toPlan(sl)
				}
			}
		}

	override suspend fun insert(plan: Plan): Plan {
		val rowId = planDao.insert(plan.toPlanEntity(sl))
		if (rowId == -1L)
			throw IllegalArgumentException("Plan(id = ${plan.id}) already exist!")
		return planDao.getByRowId(rowId)?.toPlan(sl)
			?: throw IllegalStateException("Not found plan by rowId=$rowId")
	}

	override suspend fun update(plan: Plan): Plan {
		val id = plan.id
		planDao.update(listOf(plan.toPlanEntity(sl)))
		return planDao.getPlan(id)?.toPlan(sl)
			?:throw IllegalArgumentException("Not found plan by id=$id")
	}

	override suspend fun update(plans: List<Plan>) {
		planDao.update(plans.map { it.toPlanEntity(sl) })
	}

	override suspend fun delete(plans: List<Plan>) {
		planDao.delete(plans.map { it.toPlanEntity(sl) })
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