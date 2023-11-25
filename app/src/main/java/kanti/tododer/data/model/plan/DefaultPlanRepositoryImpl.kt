package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.common.DefaultTodoRepositoryImpl
import kanti.tododer.data.model.common.TodoRepository
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource

class DefaultPlanRepositoryImpl(
	private val localDataSource: PlanLocalDataSource
) : TodoRepository<Plan> by DefaultTodoRepositoryImpl(localDataSource), PlanRepository