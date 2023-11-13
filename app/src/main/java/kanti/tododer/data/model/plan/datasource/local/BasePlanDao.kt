package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.BasePlan

interface BasePlanDao {

	suspend fun getChildren(parentId: String): List<BasePlan>

	suspend fun getByRowId(rowId: Long): BasePlan?

	suspend fun getPlan(id: Int): BasePlan?

	suspend fun update(vararg plan: BasePlan)

	suspend fun update(plan: BasePlan): Boolean

	suspend fun insert(vararg plan: BasePlan)

	suspend fun insert(plan: BasePlan): Long

	suspend fun delete(vararg plan: BasePlan)

	suspend fun delete(plan: BasePlan): Boolean

	suspend fun deleteAll()

}
