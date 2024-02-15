package kanti.tododer.domain.progress.computer

import kanti.tododer.common.logTag
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetProgressFromAllPlan @Inject constructor(
    planRepository: PlanRepository,
    private val progressRepository: ProgressRepository,
    private val computePlanProgress: ComputePlanProgress,
    @StandardLog private val logger: Logger
) {

    private val _update = MutableStateFlow(Any())

    val planAllProgress: Flow<Float> = planRepository.planAll.computeProgress("All")
    val planDefaultProgress: Flow<Float> = planRepository.planDefault.computeProgress("Default")

    val s: Flow<PlanProgress> = planRepository.standardPlans.transform {

    }
    val plansProgress: Flow<PlanProgress> = planRepository.standardPlans.run {
        channelFlow {
            logger.d(LOG_TAG, "standardPlan: start computing progress")
            collect { plans ->
                for (plan in plans) {
                    launch {
                        logger.d(LOG_TAG, "standardPlan: computing progress for $plan")
                        var progress = progressRepository.getProgress(plan.id) ?: 0f
                        send(PlanProgress(plan.id, progress))

                        progress = computePlanProgress(plan.id)
                        send(PlanProgress(plan.id, progress))
                    }
                }
            }
        }.flowOn(Dispatchers.Default)
    }

    fun update() {
        _update.value = Any()
        logger.d(LOG_TAG, "update()")
    }

    private fun Flow<Plan>.computeProgress(planName: String): Flow<Float> =
        combine(_update) { plan, _ -> plan }
            .transform { plan ->
                logger.d(logTag, "$planName: computing progress")
                var progress = progressRepository.getProgress(plan.id) ?: 0f
                emit(progress)

                progress = computePlanProgress(plan.id)
                emit(progress)
            }.flowOn(Dispatchers.Default)

    companion object {

        private const val LOG_TAG = "GetProgressFromAllPlan"
    }
}