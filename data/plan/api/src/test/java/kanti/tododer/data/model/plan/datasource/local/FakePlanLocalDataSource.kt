package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanState
import kanti.tododer.data.model.plan.toPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlanLocalDataSource(
	private val planInitializer: PlanInitializer,
	private val plans: MutableMap<Int, Plan> = LinkedHashMap()
) : PlanLocalDataSource {

	private val stateFlow: MutableStateFlow<List<Plan>> = MutableStateFlow(plans.values.toList())

	override val standardPlans: Flow<List<Plan>>
		get() = stateFlow.map { plans -> plans.filter { it.state == PlanState.Normal } }

	override suspend fun insert(plan: Plan): Plan {
		val newPlan = if (plan.id == 0) {
			val lastId = plans.keys.lastOrNull() ?: 0
			plan.toPlan(id = lastId + 1)
		} else {
			if (plans.containsKey(plan.id))
				throw IllegalArgumentException("Plan(id = ${plan.id}) already exist!")
			plan
		}
		plans[newPlan.id] = newPlan
		return newPlan
	}

	override suspend fun update(plan: Plan): Plan {
		if (!plans.containsKey(plan.id))
			throw IllegalArgumentException("Not found plan by id=${plan.id}")
		plans[plan.id] = plan
		return plan
	}

	override suspend fun update(plans: List<Plan>) {
		for (plan in plans) {
			if (!this.plans.containsKey(plan.id))
				continue
			this.plans[plan.id] = plan
		}
	}

	override suspend fun delete(plans: List<Plan>) {
		for (plan in plans) {
			this.plans.remove(plan.id)
		}
	}

	override suspend fun init() {
		if (isEmpty())
			planInitializer.initialize(this)
	}

	override suspend fun isEmpty(): Boolean {
		return plans.isEmpty()
	}

	override suspend fun clear() {
		plans.clear()
		init()
	}
}