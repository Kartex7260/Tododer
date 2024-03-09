package kanti.tododer.domain.progress.computer

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toFullId
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ComputePlanProgress @Inject constructor(
    private val todoRepository: TodoRepository,
    private val getPlanChildren: GetPlanChildren,
    private val progressRepository: ProgressRepository,
    @StandardLog private val logger: Logger
) {

    suspend operator fun invoke(planId: Long): ProgressComputer {
        val result = withContext(Dispatchers.Default) {
            val progressComputer = ProgressComputer()
            computePlanChildren(planId, progressComputer)
            launch {
                progressRepository.setProgress(planId, progressComputer.progress)
            }
            progressComputer
        }
		logger.d(LOG_TAG, "invoke(Long = $planId): return $result")
		return result
    }

    private suspend fun computePlanChildren(planId: Long, progressComputer: ProgressComputer) {
        val children = getPlanChildren(planId)
        for (child in children) {
            progressComputer.add(child.id, child.done)
            if (!child.done)
                computeTodoChildren(child.toFullId(), progressComputer)
        }
		logger.d(
			LOG_TAG,
			"computePlanChildren(Long = $planId, ProgressComputer = $progressComputer)"
		)
    }

    private suspend fun computeTodoChildren(
        todoFullId: FullId,
        progressComputer: ProgressComputer
    ) {
        val children = todoRepository.getChildren(todoFullId)
        for (child in children) {
            progressComputer.add(child.id, child.done)
            if (!child.done)
                computeTodoChildren(child.toFullId(), progressComputer)
        }
		logger.d(
			LOG_TAG,
			"computeTodoChildren(FullId = $todoFullId, ProgressComputer = $progressComputer)"
		)
    }

	companion object {

		private const val LOG_TAG = "ComputePlanProgress"
	}
}