package kanti.tododer.data.model.progress.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.progress.BaseTodoProgress
import kanti.tododer.data.model.progress.toTodoProgress

@Dao
interface TodoProgressDao : BaseTodoProgressDao {

	override suspend fun getTodoProgress(fullId: String): BaseTodoProgress? {
		return getTodoProgressRoom(fullId)?.toTodoProgress()
	}

	@Query("SELECT * FROM todo_progress_cache WHERE fullId = :fullId")
	suspend fun getTodoProgressRoom(fullId: String): TodoProgressEntity?

	override suspend fun getTodoProgressByRowId(rowId: Long): BaseTodoProgress? {
		return getTodoProgressByRowIdRoom(rowId)?.toTodoProgress()
	}

	@Query("SELECT * FROM todo_progress_cache WHERE rowId = :rowId")
	suspend fun getTodoProgressByRowIdRoom(rowId: Long): TodoProgressEntity?

	override suspend fun insert(planProgress: BaseTodoProgress): Long {
		return insertRoom(planProgress.toTodoProgressEntity())
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertRoom(planProgress: TodoProgressEntity): Long

	override suspend fun delete(planProgress: BaseTodoProgress): Int {
		return deleteRoom(planProgress.toTodoProgressEntity())
	}

	@Delete
	suspend fun deleteRoom(planProgress: TodoProgressEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM todo_progress_cache")
	suspend fun deleteAllRoom()

}
