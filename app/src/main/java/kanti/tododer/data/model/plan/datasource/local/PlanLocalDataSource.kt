package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.Plan

interface PlanLocalDataSource {

	suspend fun getPlan(id: Int): LocalResult<BasePlan>

	suspend fun getChildren(fid: String): LocalResult<List<BasePlan>>

	suspend fun insert(plan: BasePlan): LocalResult<BasePlan>

	suspend fun replace(plan: BasePlan): LocalResult<BasePlan>

	suspend fun delete(plan: BasePlan): Boolean

	suspend fun deleteAll()

}