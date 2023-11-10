package kanti.tododer.data.model.plan.datasource.local

import android.media.Image.Plane
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.toPlan

@Dao
interface PlanDao : BasePlanDao {


	override suspend fun getChildren(parentId: String): List<Plan> {
		return getChildrenRoom(parentId).map { it.toPlan() }
	}

	@Query("SELECT * FROM `plan` WHERE parent_id = :parentId")
	fun getChildrenRoom(parentId: String): List<PlanEntity>

	override suspend fun getByRowId(rowId: Long): Plan? {
		return getByRowIdRoom(rowId)?.toPlan()
	}

	@Query("SELECT * FROM `plan` WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): PlanEntity?

	override suspend fun getPlan(id: Int): Plan? {
		return getPlanRoom(id)?.toPlan()
	}

	@Query("SELECT * FROM `plan` WHERE id = :id")
	suspend fun getPlanRoom(id: Int): PlanEntity?

	override suspend fun replace(plan: Plan): Long {
		return replaceRoom(plan.toPlanEntity())
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(plan: PlanEntity): Long

	override suspend fun insert(plan: Plan): Long {
		return insertRoom(plan.toPlanEntity())
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(plan: PlanEntity): Long

	override suspend fun delete(plan: Plan): Int {
		return deleteRoom(plan.toPlanEntity())
	}

	@Delete
	suspend fun deleteRoom(plan: PlanEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM `plan`")
	suspend fun deleteAllRoom()

}