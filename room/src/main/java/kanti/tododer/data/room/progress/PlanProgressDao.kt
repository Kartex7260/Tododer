package kanti.tododer.data.room.progress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlanProgressDao {

	@Query("SELECT progress FROM plan_progress WHERE plan_id = :planId LIMIT 1")
	suspend fun getProgress(planId: Long): Float?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(progress: PlanProgressEntity)

	@Query("DELETE FROM plan_progress WHERE plan_id = :planId LIMIT 1")
	suspend fun deleteProgress(planId: Long)
}