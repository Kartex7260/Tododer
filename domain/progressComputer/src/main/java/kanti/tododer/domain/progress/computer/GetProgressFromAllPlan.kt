package kanti.tododer.domain.progress.computer

import kanti.tododer.common.Const
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProgressFromAllPlan @Inject constructor(
    private val planRepository: PlanRepository,
    private val progressRepository: ProgressRepository,
    private val computePlanProgress: ComputePlanProgress,
    @StandardLog private val logger: Logger
) {

    private val _planAllProgress = MutableStateFlow(0f)
    val planAllProgress: StateFlow<Float> = _planAllProgress.asStateFlow()

    private val _planDefaultProgress = MutableStateFlow(0f)
    val planDefaultProgress: StateFlow<Float> = _planDefaultProgress

    private val _plansProgress = MutableStateFlow<List<PlanProgress>>(listOf())
    val plansProgress: StateFlow<List<PlanProgress>> = _plansProgress.asStateFlow()

    private val _totalTodos = MutableStateFlow(0)
    val totalTodos: StateFlow<Int> = _totalTodos.asStateFlow()

    suspend operator fun invoke() {
        logger.d(LOG_TAG, "invoke()")
        withContext(Dispatchers.Default) {
            launch {
                val planId = Const.PlansIds.ALL
                logger.d(LOG_TAG, "computing PlanAll")

                var progress = progressRepository.getProgress(planId)
                logger.d(LOG_TAG, "computing PlanAll: get from db=$progress")
                _planAllProgress.value = progress ?: 0f

                val progressComputer = computePlanProgress(planId)
                logger.d(LOG_TAG, "computing PlanAll: result=$progressComputer")
                _totalTodos.value = progressComputer.all

                progress = progressComputer.progress
                _planAllProgress.value = progress
                progressRepository.setProgress(planId, progress)
            }

            launch {
                val planId = Const.PlansIds.DEFAULT
                logger.d(LOG_TAG, "computing PlanDefault")

                val progress = progressRepository.getProgress(planId)
                logger.d(LOG_TAG, "computing PlanDefault: get from db=$progress")
                _planDefaultProgress.value = progress ?: 0f

                val progressComputer = computePlanProgress(planId)
                logger.d(LOG_TAG, "computing PlanDefault: result=$progressComputer")

                val computedProgress = progressComputer.progress
                _planDefaultProgress.value = computedProgress
            }

            val plans = planRepository.getStandardPlans()
            val jobs = mutableListOf<Job>()
            var progresses = mutableListOf<PlanProgress>()
            val mutex = Mutex()
            for (plan in plans) {
                val job = launch {
                    val progress = progressRepository.getProgress(plan.id) ?: 0f
                    logger.d(LOG_TAG, "computing ${plan.title}: get from db=$progress")
                    mutex.withLock {
                        progresses.add(PlanProgress(plan.id, progress))
                    }
                }
                jobs.add(job)
            }
            joinAll(*jobs.toTypedArray())
            logger.d(LOG_TAG, "plansProgress: from db ${
                progresses.joinToString(prefix = "(ID, PROGRESS)[", postfix = "]") { 
                    "(${it.planId}, ${it.progress})"
                }
            }")
            _plansProgress.value = progresses

            progresses = mutableListOf<PlanProgress>()
            jobs.clear()
            for (plan in plans) {
                val job = launch {
                    logger.d(LOG_TAG, "computing ${plan.title}")

                    val progressComputer = computePlanProgress(plan.id)
                    logger.d(LOG_TAG, "computing ${plan.title}: result=$progressComputer")

                    val computedProgress = progressComputer.progress
                    mutex.withLock { progresses.add(PlanProgress(plan.id, computedProgress)) }
                    progressRepository.setProgress(plan.id, computedProgress)
                }
                jobs.add(job)
            }
            joinAll(*jobs.toTypedArray())
            logger.d(LOG_TAG, "plansProgress: computing ${
                progresses.joinToString(prefix = "(ID, PROGRESS)[", postfix = "]") {
                    "(${it.planId}, ${it.progress})"
                }
            }")
            _plansProgress.value = progresses
        }
    }

    companion object {

        private const val LOG_TAG = "GetProgressFromAllPlan"
    }
}