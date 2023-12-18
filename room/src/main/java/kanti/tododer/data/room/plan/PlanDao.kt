package kanti.tododer.data.room.plan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

	@Query("SELECT * FROM plans WHERE archived = :archived")
	fun getAll(archived: Boolean): Flow<List<PlanEntity>>

	@Query("SELECT * FROM plans WHERE rowId = :rowId LIMIT 1")
	suspend fun getByRowId(rowId: Long): PlanEntity?

	@Query("SELECT * FROM plans WHERE id = :id LIMIT 1")
	suspend fun getPlan(id: Int): PlanEntity?

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(plan: PlanEntity): Long

	@Update(onConflict = OnConflictStrategy.REPLACE)
	suspend fun update(plans: List<PlanEntity>)

	@Delete
	suspend fun delete(plans: List<PlanEntity>)

	@Query("SELECT COUNT(*) FROM plans")
	suspend fun count(): Int

	@Query("DELETE FROM plans")
	suspend fun deleteAll()
}