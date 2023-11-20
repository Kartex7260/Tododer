package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.plan.Plan

interface PlanLocalDataSource {

	suspend fun getPlan(id: Int): LocalResult<Plan>

	suspend fun getChildren(fid: String): LocalResult<List<Plan>>

	suspend fun insert(vararg plan: Plan): LocalResult<Unit>

	suspend fun insert(plan: Plan): LocalResult<Plan>

	suspend fun update(vararg plan: Plan): LocalResult<Unit>

	suspend fun update(plan: Plan): LocalResult<Plan>

	suspend fun delete(vararg plan: Plan): LocalResult<Unit>

	suspend fun delete(plan: Plan): Boolean

	suspend fun deleteAll()

}