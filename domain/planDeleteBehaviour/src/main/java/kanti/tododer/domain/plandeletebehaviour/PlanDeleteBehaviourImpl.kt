package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.toFullId
import kanti.tododer.data.model.todo.TodoRepository
import javax.inject.Inject

class PlanDeleteBehaviourImpl @Inject constructor(
	private val planRepository: PlanRepository,
	private val todoRepository: TodoRepository
) : PlanDeleteBehaviour {

	override suspend fun delete(plansId: List<Int>): List<Plan> {
		return planRepository.delete(plansId)
	}

	override suspend fun undoDelete() {
		planRepository.undoDelete()
	}

	override suspend fun undoChanceRejected() {
		val deletedPlans = planRepository.undoChanceRejected() ?: return
		for (deletedPlan in deletedPlans) {
			todoRepository.deleteChildren(deletedPlan.toFullId())
		}
	}
}