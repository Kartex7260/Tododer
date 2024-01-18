package kanti.tododer.domain.progress.computer

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toFullId
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ComputePlanProgress @Inject constructor(
	private val todoRepository: TodoRepository,
	private val getPlanChildren: GetPlanChildren,
	private val progressRepository: ProgressRepository
) {

	suspend operator fun invoke(planId: Long): Float {
		return coroutineScope {
			val progressComputer = ProgressComputer()
			computePlanChildren(planId, progressComputer)
			val progress = progressComputer.progress
			launch {
				progressRepository.setProgress(planId, progress)
			}
			progress
		}
	}

	private suspend fun computePlanChildren(planId: Long, progressComputer: ProgressComputer) {
		val children = getPlanChildren(planId)
		for (child in children) {
			progressComputer.add(child.id, child.done)
			computeTodoChildren(child.toFullId(), progressComputer)
		}
	}

	private suspend fun computeTodoChildren(todoFullId: FullId, progressComputer: ProgressComputer) {
		val children = todoRepository.getChildren(todoFullId)
		for (child in children) {
			progressComputer.add(child.id, child.done)
			computeTodoChildren(child.toFullId(), progressComputer)
		}
	}
}