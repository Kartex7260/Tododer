package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.plan.PlanEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlanDao(
	val plans: MutableMap<Int, PlanEntity> = LinkedHashMap()
) : PlanDao {

	private val sharedFlow: MutableStateFlow<List<PlanEntity>> = MutableStateFlow(plans.values.toList())

	override fun getAll(archived: Boolean): Flow<List<PlanEntity>> {
		sharedFlow.value = plans.values.toList()
		return sharedFlow.map {
			plans -> plans.filter {
				it.archived == archived
			}
		}
	}

	override suspend fun getByRowId(rowId: Long): PlanEntity? {
		return plans[rowId.toInt()]
	}

	override suspend fun getPlan(id: Int): PlanEntity? {
		return plans[id]
	}

	override suspend fun insert(plan: PlanEntity): Long {
		val newPlan = if (plan.id == 0) {
			val lastId = plans.keys.lastOrNull() ?: 0
			plan.copy(id = lastId + 1)
		} else {
			if (plans.containsKey(plan.id))
				return -1L
			plan
		}
		plans[newPlan.id] = newPlan
		sharedFlow.value = plans.values.toList()
		return newPlan.id.toLong()
	}

	override suspend fun update(plans: List<PlanEntity>) {
		for (plan in plans) {
			if (this.plans.containsKey(plan.id)) {
				this.plans[plan.id] = plan
			}
		}
	}

	override suspend fun delete(plans: List<PlanEntity>) {
		for (plan in plans) {
			this.plans.remove(plan.id)
		}
	}

	override suspend fun count(): Int {
		return plans.size
	}

	override suspend fun deleteAll() {
		plans.clear()
	}
}