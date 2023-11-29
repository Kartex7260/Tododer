package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.FakePlanRoomDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanEntity

class FakePlanRepository(
	val plans: MutableList<PlanEntity> = mutableListOf()
) : PlanRepository by DefaultPlanRepositoryImpl(FakePlanRoomDataSource(plans))