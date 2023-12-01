package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.model.plan.Plan

interface PlanLocalDataSource {

	suspend fun getPlan(id: Int): GetLocalResult<Plan>

	suspend fun getChildren(fid: String): Result<List<Plan>>

	suspend fun insert(plan: Plan): Result<Plan>

	suspend fun insert(vararg plan: Plan): Result<Unit>

	suspend fun delete(vararg plan: Plan): Result<Unit>

	suspend fun deleteAll(): Result<Unit>

}