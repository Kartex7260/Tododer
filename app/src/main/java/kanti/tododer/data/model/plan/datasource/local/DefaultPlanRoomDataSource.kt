package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.datasource.local.DefaultTodoDaoDataSourceImpl
import kanti.tododer.data.model.common.datasource.local.TodoLocalDataSource
import kanti.tododer.data.model.plan.Plan

class DefaultPlanRoomDataSource(
	private val planDao: BasePlanDao
) : TodoLocalDataSource<Plan> by DefaultTodoDaoDataSourceImpl(planDao), PlanLocalDataSource