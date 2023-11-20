package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan

interface BasePlanDao {

	suspend fun getChildren(parentId: String): List<Plan>

	suspend fun getByRowId(rowId: Long): Plan?

	suspend fun getPlan(id: Int): Plan?

	suspend fun update(vararg plan: Plan)

	suspend fun update(plan: Plan): Boolean

	suspend fun insert(vararg plan: Plan)

	suspend fun insert(plan: Plan): Long

	suspend fun delete(vararg plan: Plan)

	suspend fun delete(plan: Plan): Boolean

	suspend fun deleteAll()

}
