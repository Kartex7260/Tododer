package kanti.tododer.common.features

import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.toTask
import kotlinx.coroutines.launch

interface TaskIsDoneFeature : CoroutineScopeFeature, TaskRepositoryFeature {

	fun taskIsDone(task: Task, isDone: Boolean) {
		coroutineScope.launch {
			taskRepository.update(task) {
				toTask(
					done = isDone
				)
			}
		}
	}

}