package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletePlanIfBlank @Inject constructor(
    private val planRepository: PlanRepository,
    private val todoRepository: TodoRepository,
    private val appDataRepository: AppDataRepository,
    @StandardLog private val logger: Logger
) {

    private val _planDeleted = MutableSharedFlow<Unit>()
    val planDeleted = _planDeleted.asSharedFlow()

    suspend operator fun invoke(planFullId: FullId) {
        if (todoRepository.getChildrenCount(planFullId) != 0L) {
            logger.d(LOG_TAG, "invoke(FullId = $planFullId): children found")
            return
        }
        val deleted = planRepository.deletePlanIfNameIsEmpty(planFullId.id)
        if (deleted) {
            appDataRepository.deleteIfCurrent(planFullId.id)
            _planDeleted.emit(Unit)
        }
        logger.d(LOG_TAG, "invoke(FullId = $planFullId): emit = $deleted")
    }

    companion object {

        private const val LOG_TAG = "DeletePlanIfBlank"
    }
}