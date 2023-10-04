package kanti.tododer.data.model.plan.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.asNewPlanEntity
import kanti.tododer.data.model.plan.asPlanEntity

@Dao
abstract class PlanDao {

	@Query("SELECT * FROM `plan` WHERE parent_id = :parentId")
	abstract suspend fun getChildren(parentId: String): List<PlanEntity>

	@Query("SELECT 1 FROM `plan` WHERE rowid = :rowId")
	abstract suspend fun getByRowId(rowId: Long): PlanEntity

	@Query("SELECT * FROM `plan` WHERE id = :id")
	abstract suspend fun getById(id: Int): List<PlanEntity>

	suspend fun get(id: Int): PlanEntity? {
		val plans = getById(id)
		if (plans.isEmpty())
			return null
		return plans[0]
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun replace(plan: PlanEntity): Long

	suspend fun replace(plan: Plan): Long = replace(plan.asPlanEntity)

	suspend fun insert(plan: Plan): Long = replace(plan.asNewPlanEntity)


	@Delete
	abstract suspend fun delete(plan: PlanEntity): Int

	suspend fun delete(plan: Plan): Int = delete(plan.asPlanEntity)

}