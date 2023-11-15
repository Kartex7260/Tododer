package kanti.tododer.common.features

import kanti.tododer.data.model.task.TaskRepository

interface TaskRepositoryFeature {

	val taskRepository: TaskRepository

}