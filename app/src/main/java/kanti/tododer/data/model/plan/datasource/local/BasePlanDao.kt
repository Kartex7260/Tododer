package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan

interface BasePlanDao {

	suspend fun getChildren(parentId: String): List<Plan>

	suspend fun getByRowId(rowId: Long): Plan?

	suspend fun getPlan(id: Int): Plan?

	suspend fun replace(plan: Plan): Long

	suspend fun insert(plan: Plan): Long

	suspend fun delete(plan: Plan): Int

	suspend fun deleteAll()

}
