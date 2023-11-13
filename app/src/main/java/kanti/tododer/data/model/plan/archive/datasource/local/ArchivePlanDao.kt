package kanti.tododer.data.model.plan.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.archive.toArchivePlan
import kanti.tododer.data.model.plan.datasource.local.BasePlanDao

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

	override suspend fun insert(vararg plan: BasePlan) {
		insertRoom(*plan.map { it.toArchivePlanEntity() }.toTypedArray())
	}

	override suspend fun insert(plan: BasePlan): Long {
		return insertRoom(plan.toArchivePlanEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg plan: ArchivePlanEntity): Array<Long>

	override suspend fun update(vararg plan: BasePlan) {
		updateRoom(*plan.map { it.toArchivePlanEntity() }.toTypedArray())
	}

	override suspend fun update(plan: BasePlan): Boolean {
		return updateRoom(plan.toArchivePlanEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg plan: ArchivePlanEntity): Int

	override suspend fun delete(vararg plan: BasePlan) {
		deleteRoom(*plan.map { it.toArchivePlanEntity() }.toTypedArray())
	}

	override suspend fun delete(plan: BasePlan): Boolean {
		return deleteRoom(plan.toArchivePlanEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg plan: ArchivePlanEntity): Int

	override suspend fun deleteAll() = deleteAllRoom()

	@Query("DELETE FROM archive_plan")
	suspend fun deleteAllRoom()

}