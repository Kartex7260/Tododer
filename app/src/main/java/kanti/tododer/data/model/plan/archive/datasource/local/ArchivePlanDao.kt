package kanti.tododer.data.model.plan.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.datasource.local.BasePlanDao
import kanti.tododer.data.model.plan.toPlan

@Dao
interface ArchivePlanDao : BasePlanDao {

	override suspend fun getChildren(parentId: String): List<Plan> {
		return getChildrenRoom(parentId).map { it.toPlan() }
	}

	@Query("SELECT * FROM archive_plan WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<ArchivePlanEntity>

	override suspend fun getByRowId(rowId: Long): Plan? {
		return getByRowIdRoom(rowId)?.toPlan()
	}

	@Query("SELECT * FROM archive_plan WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): ArchivePlanEntity?

	override suspend fun getPlan(id: Int): Plan? {
		return getPlanRoom(id)?.toPlan()
	}

	@Query("SELECT * FROM archive_plan WHERE id = :id")
	suspend fun getPlanRoom(id: Int): ArchivePlanEntity?

	override suspend fun replace(plan: Plan): Long {
		return replaceRoom(plan.toArchivePlanEntity())
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(plan: ArchivePlanEntity): Long

	override suspend fun insert(plan: Plan): Long {
		return insertRoom(plan.toArchivePlanEntity())
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(plan: ArchivePlanEntity): Long

	override suspend fun delete(plan: Plan): Int {
		return deleteRoom(plan.toArchivePlanEntity())
	}

	@Delete
	suspend fun deleteRoom(plan: ArchivePlanEntity): Int

	override suspend fun deleteAll() = deleteAllRoom()

	@Query("DELETE FROM archive_plan")
	suspend fun deleteAllRoom()

}