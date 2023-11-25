package kanti.tododer.data.model.plan.datasource.local

class FakePlanRoomDataSource : PlanLocalDataSource by DefaultPlanRoomDataSource(FakePlanDao())