package kanti.tododer.data.model.plan.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.archive.toArchivePlan
import kanti.tododer.data.model.plan.datasource.local.BasePlanDao
import kanti.tododer.data.model.plan.toPlan

@Dao
interface ArchivePlanDao : BasePlanDao {

	override suspend fun getChildren(parentId: String): List<BasePlan> {
		return getChildrenRoom(parentId).map { it.toArchivePlan() }
	}

	@Query("SELECT * FROM archive_plan WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<ArchivePlanEntity>

	override suspend fun getByRowId(rowId: Long): BasePlan? {
		return getByRowIdRoom(rowId)?.toArchivePlan()
	}

	@Query("SELECT * FROM archive_plan WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): ArchivePlanEntity?

	override suspend fun getPlan(id: Int): BasePlan? {
		return getPlanRoom(id)?.toArchivePlan()
	}

	@Query("SELECT * FROM archive_plan WHERE id = :id")
	suspend fun getPlanRoom(id: Int): ArchivePlanEntity?

	override suspend fun replace(plan: BasePlan): Long {
		return replaceRoom(plan.toArchivePlanEntity())
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(plan: ArchivePlanEntity): Long

	override suspend fun replace(list: List<BasePlan>) {
		replaceRoom(list.map { it.toArchivePlanEntity() })
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(list: List<ArchivePlanEntity>)

	override suspend fun insert(plan: BasePlan): Long {
		return insertRoom(plan.toArchivePlanEntity())
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(plan: ArchivePlanEntity): Long

	override suspend fun insert(list: List<BasePlan>) {
		insertRoom(list.map { it.toArchivePlanEntity() })
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(list: List<ArchivePlanEntity>)

	override suspend fun delete(plan: BasePlan): Int {
		return deleteRoom(plan.toArchivePlanEntity())
	}

	@Delete
	suspend fun deleteRoom(plan: ArchivePlanEntity): Int

	override suspend fun delete(list: List<BasePlan>) {
		deleteRoom(list.map { it.toArchivePlanEntity() })
	}

	@Delete
	suspend fun deleteRoom(list: List<ArchivePlanEntity>)

	override suspend fun deleteAll() = deleteAllRoom()

	@Query("DELETE FROM archive_plan")
	suspend fun deleteAllRoom()

}