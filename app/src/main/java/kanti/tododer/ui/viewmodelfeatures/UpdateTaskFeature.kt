package kanti.tododer.ui.viewmodelfeatures

import kanti.tododer.data.common.isNull
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.TaskRepository

interface UpdateTaskFeature : TaskRepositoryFeature {

	suspend fun updateTask(id: Int, body: BaseTask.() -> BaseTask) {
		val task = taskRepository.getTask(id).also { repositoryResult ->
			if (!repositoryResult.isSuccess || repositoryResult.isNull)
				return
		}.value!!
		taskRepository.update(task, body)
	}

}