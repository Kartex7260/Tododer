package kanti.tododer.data.model

import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository

data class RepositorySet(
	val taskRepository: TaskRepository,
	val planRepository: PlanRepository
)
