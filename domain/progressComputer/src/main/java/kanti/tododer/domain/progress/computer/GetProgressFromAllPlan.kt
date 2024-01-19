package kanti.tododer.domain.progress.computer

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.data.model.progress.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetProgressFromAllPlan @Inject constructor(
	planRepository: PlanRepository,
	private val progressRepository: ProgressRepository,
	private val computePlanProgress: ComputePlanProgress
) {

	private val _update = MutableStateFlow(Any())

	val planAllProgress: Flow<Float> = planRepository.planAll.computeProgress()
	val planDefaultProgress: Flow<Float> = planRepository.planDefault.computeProgress()

	val plansProgress: Flow<PlanProgress> = channelFlow {
		planRepository.standardPlans.collect { plans ->
			for (plan in plans) {
				launch {
					var progress = progressRepository.getProgress(plan.id) ?: 0f
					channel.send(PlanProgress(plan.id, progress))

					progress = computePlanProgress(plan.id)
					channel.send(PlanProgress(plan.id, progress))
				}
			}
		}
	}

	fun update() {
		_update.value = Any()
	}

	private fun Flow<Plan>.computeProgress(): Flow<Float> = combine(_update) { plan, _ -> plan }
			.transform { plan ->
				var progress = progressRepository.getProgress(plan.id) ?: 0f
				emit(progress)

				progress = computePlanProgress(plan.id)
				emit(progress)
			}
}