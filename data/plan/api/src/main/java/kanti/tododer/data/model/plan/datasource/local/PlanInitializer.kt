package kanti.tododer.data.model.plan.datasource.local

interface PlanInitializer {

	suspend fun initialize(dataSource: PlanLocalDataSource)
}