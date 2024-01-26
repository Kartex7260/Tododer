package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletePlanIfBlank @Inject constructor(
    private val planRepository: PlanRepository,
    private val todoRepository: TodoRepository,
    private val appDataRepository: AppDataRepository
) {

    private val _planDeleted = MutableSharedFlow<Unit>()
    val planDeleted = _planDeleted.asSharedFlow()

    suspend operator fun invoke(planFullId: FullId) {
        if (todoRepository.getChildrenCount(planFullId) == 0L)
            return
        planRepository.deletePlanIfNameIsEmpty(planFullId.id)
        appDataRepository.deleteIfCurrent(planFullId.id)
        _planDeleted.emit(Unit)
    }
}