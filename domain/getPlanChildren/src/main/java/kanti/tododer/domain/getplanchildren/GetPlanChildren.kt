package kanti.tododer.domain.getplanchildren

import kanti.tododer.common.Const
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toFullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import javax.inject.Inject

class GetPlanChildren @Inject constructor(
	private val planRepository: PlanRepository,
	private val todoRepository: TodoRepository,
	@StandardLog private val logger: Logger
) {

	suspend operator fun invoke(planId: Long): List<Todo> {
		val plan = planRepository.getPlan(planId) ?: run {
			logger.d(LOG_TAG, "invoke(Long = $planId): not found plan, return empty list")
			return listOf()
		}
		val result = when (plan.type) {
			PlanType.All -> {
				val todos = mutableListOf<Todo>()
				val defaultPlan = FullId(Const.PlansIds.DEFAULT, FullIdType.Plan)
				val defaultChildren = todoRepository.getChildren(defaultPlan)
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
		logger.d(LOG_TAG, "invoke(Long = $planId): return count(${result.size})")
		return result
	}

	companion object {

		private const val LOG_TAG = "GetPlanChildren"
	}
}