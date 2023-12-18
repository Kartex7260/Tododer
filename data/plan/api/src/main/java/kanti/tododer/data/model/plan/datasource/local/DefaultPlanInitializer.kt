package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import javax.inject.Inject

class DefaultPlanInitializer @Inject constructor() : PlanInitializer {

	override suspend fun initialize(dataSource: PlanLocalDataSource) {
		dataSource.insert(Plan(
			type = PlanType.All
		))
		dataSource.insert(Plan(
			type = PlanType.Default
		))
	}
}