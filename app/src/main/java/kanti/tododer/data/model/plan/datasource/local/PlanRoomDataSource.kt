package kanti.tododer.data.model.plan.datasource.local

import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	planDao: PlanDao
) : PlanLocalDataSource by DefaultPlanRoomDataSource(planDao)