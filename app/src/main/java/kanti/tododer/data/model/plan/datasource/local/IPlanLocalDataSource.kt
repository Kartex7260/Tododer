package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.plan.Plan

interface IPlanLocalDataSource {

	suspend fun getPlan(id: Int): LocalResult<Plan>

	suspend fun getChildren(fid: String): LocalResult<List<Plan>>

	suspend fun insert(plan: Plan): LocalResult<Plan>

	suspend fun replace(plan: Plan): LocalResult<Plan>

	suspend fun delete(plan: Plan): Boolean

	suspend fun deleteAll()

}