package kanti.tododer.domain.todomove

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.RepositorySet
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.domain.gettodowithprogeny.plan.GetPlanWithProgenyUseCase
import kanti.tododer.domain.gettodowithprogeny.plan.PlanWithProgeny
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovePlanUseCase @Inject constructor(
	private val getPlanWithProgenyUseCase: GetPlanWithProgenyUseCase
) {

	suspend operator fun invoke(
		from: RepositorySet,
		to: RepositorySet,
		plan: Plan
	): RepositoryResult<Unit> {
		val planWithProgeny = getPlanWithProgenyUseCase(from, plan)
		if (!planWithProgeny.isSuccess || planWithProgeny.value == null)
			return RepositoryResult(type = planWithProgeny.type)

		return coroutineScope {
			val removeJob = launch {
				removeFrom(from, planWithProgeny.value)
			}
			val addJob = launch {
				addTo(to, planWithProgeny.value)
			}
			listOf(removeJob, addJob).joinAll()
			RepositoryResult()
		}
	}

	private suspend fun removeFrom(from: RepositorySet, planWithProgeny: PlanWithProgeny) {
		from.apply {
			planRepository.delete(*planWithProgeny.plans.toTypedArray())
			taskRepository.delete(*planWithProgeny.tasks.toTypedArray())
		}
	}

	private suspend fun addTo(to: RepositorySet, planWithProgeny: PlanWithProgeny) {
		to.apply {
			planRepository.insert(*planWithProgeny.plans.toTypedArray())
			taskRepository.insert(*planWithProgeny.tasks.toTypedArray())
		}
	}

}