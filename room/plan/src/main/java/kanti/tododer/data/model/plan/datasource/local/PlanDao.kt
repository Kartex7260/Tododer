package kanti.tododer.data.model.plan.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlanDao {

	@Query("SELECT * FROM `plan` WHERE parent_id = :parentId")
	suspend fun getChildren(parentId: String): List<PlanEntity>

	@Query("SELECT * FROM `plan` WHERE rowid = :rowId")
	suspend fun getByRowId(rowId: Long): PlanEntity?

	@Query("SELECT * FROM `plan` WHERE id = :id")
	suspend fun getPlan(id: Int): PlanEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg plan: PlanEntity): Array<Long>

	@Delete
	suspend fun delete(vararg plan: PlanEntity): Int

	@Query("DELETE FROM `plan`")
	suspend fun deleteAll()

}
