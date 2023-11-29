package kanti.tododer.data.model.plan.datasource.local

class FakePlanRoomDataSource(
	val plans: MutableList<PlanEntity> = mutableListOf()
) : PlanLocalDataSource by DefaultPlanRoomDataSource(FakePlanDao(plans))