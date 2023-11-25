package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.FakePlanRoomDataSource

class FakePlanRepository : PlanRepository by DefaultPlanRepositoryImpl(FakePlanRoomDataSource())