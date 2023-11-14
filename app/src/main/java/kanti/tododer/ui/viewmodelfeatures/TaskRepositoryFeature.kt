package kanti.tododer.ui.viewmodelfeatures

import kanti.tododer.data.model.task.TaskRepository

interface TaskRepositoryFeature {

	val taskRepository: TaskRepository

}