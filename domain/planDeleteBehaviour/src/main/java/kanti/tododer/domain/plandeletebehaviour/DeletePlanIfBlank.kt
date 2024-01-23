package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import javax.inject.Inject

class DeletePlanIfBlank @Inject constructor(
    private val planRepository: PlanRepository,
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(planFullId: FullId) {
        if (todoRepository.getChildrenCount(planFullId) == 0L)
            return
        planRepository.deletePlanIfNameIsEmpty(planFullId.id)
    }
}