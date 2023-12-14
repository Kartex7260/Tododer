package kanti.tododer.data.room.plan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

	@Query("SELECT * FROM plans WHERE archived = :archived")
	fun getAll(archived: Boolean): Flow<List<PlanEntity>>

	@Insert
	suspend fun insert(vararg plan: PlanEntity)

	@Update
	suspend fun update(vararg plan: PlanEntity)

	@Delete
	suspend fun delete(vararg plan: PlanEntity)

	@Query("SELECT Count(*) FROM plans")
	suspend fun count(): Int

	@Query("DELETE FROM plans")
	suspend fun deleteAll()
}
