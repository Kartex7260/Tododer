package kanti.tododer.common.features

import kanti.tododer.data.common.isNull
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.task.Task

interface UpdateTaskFeature : TaskRepositoryFeature {

	suspend fun updateTask(id: Int, body: Task.() -> Task) {
		val task = taskRepository.getTask(id).also { repositoryResult ->
			if (!repositoryResult.isSuccess || repositoryResult.isNull)
				return
		}.value!!
		taskRepository.update(task, body)
	}

}