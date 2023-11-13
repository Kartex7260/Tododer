package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.BasePlan

interface BasePlanDao {

	suspend fun getChildren(parentId: String): List<BasePlan>

	suspend fun getByRowId(rowId: Long): BasePlan?

	suspend fun getPlan(id: Int): BasePlan?

	suspend fun replace(plan: BasePlan): Long

	suspend fun replace(list: List<BasePlan>)

	suspend fun insert(plan: BasePlan): Long

	suspend fun insert(list: List<BasePlan>)

	suspend fun delete(plan: BasePlan): Int

	suspend fun delete(list: List<BasePlan>)

	suspend fun deleteAll()

}
