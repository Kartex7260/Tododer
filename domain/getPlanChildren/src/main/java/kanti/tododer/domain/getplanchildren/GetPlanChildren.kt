package kanti.tododer.domain.getplanchildren

import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toFullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoRepository
import javax.inject.Inject

class GetPlanChildren @Inject constructor(
	private val planRepository: PlanRepository,
	private val todoRepository: TodoRepository
) {

	suspend operator fun invoke(planId: Int): List<Todo> {
		val plan = planRepository.getPlan(planId) ?: return listOf()
		return when (plan.type) {
			PlanType.All -> {
				val todos = mutableListOf<Todo>()
				val defaultPlan = planRepository.getDefaultPlan()
				val defaultChildren = todoRepository.getChildren(defaultPlan.toFullId())
				todos.addAll(defaultChildren)

				val allPlans = planRepository.getStandardPlans()
				for (pln in allPlans) {
					val planChildren = todoRepository.getChildren(pln.toFullId())
					todos.addAll(planChildren)
				}
				todos
			}
			else -> { todoRepository.getChildren(plan.toFullId()) }
		}
	}
}