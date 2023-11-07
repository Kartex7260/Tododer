package kanti.tododer.data.model.plan.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.plan.Plan

@Dao
abstract class IPlanDao {

	@Query("SELECT * FROM `plan` WHERE parent_id = :parentId")
	abstract suspend fun getChildren(parentId: String): List<PlanEntity>

	@Query("SELECT * FROM `plan` WHERE rowid = :rowId")
	abstract suspend fun getByRowId(rowId: Long): PlanEntity?

	@Query("SELECT * FROM `plan` WHERE id = :id")
	abstract suspend fun getPlan(id: Int): PlanEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun replace(plan: PlanEntity): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	abstract suspend fun insert(plan: PlanEntity): Long


	@Delete
	abstract suspend fun delete(plan: PlanEntity): Int

	suspend fun delete(plan: Plan): Int = delete(plan.toPlanEntity())

	@Query("DELETE FROM `plan`")
	abstract suspend fun deleteAll()

}

suspend fun IPlanDao.insert(plan: Plan): Long = insert(plan.toPlanEntity())

suspend fun IPlanDao.replace(plan: Plan): Long = replace(plan.toPlanEntity())