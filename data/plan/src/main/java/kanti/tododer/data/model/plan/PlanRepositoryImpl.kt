package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
	private val localDataSource: PlanLocalDataSource
) : PlanRepository {

	override val standardPlans: Flow<List<Plan>>
		get() = localDataSource.standardPlans
	override val archivedPlans: Flow<List<Plan>>
		get() = localDataSource.archivedPlans

	override suspend fun insert(vararg plan: Plan) {
		localDataSource.insert(*plan)
	}

	override suspend fun update(vararg plan: Plan) {
		localDataSource.update(*plan)
	}

	override suspend fun delete(vararg plan: Plan) {
		localDataSource.delete(*plan)
	}

	override suspend fun isEmpty(): Boolean {
		return localDataSource.isEmpty()
	}

	override suspend fun clear() {
		localDataSource.clear()
	}
}