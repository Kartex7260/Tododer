package kanti.tododer.data.room.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {

	@Query("SELECT * FROM todos WHERE parent_id = :parentId")
	suspend fun getChildren(parentId: String): List<TodoEntity>

	@Query("DELETE FROM todos WHERE parent_id = :parentId")
	suspend fun deleteChildren(parentId: String)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(todo: TodoEntity): Long

	@Query("SELECT * FROM todos WHERE rowId = :rowId LIMIT 1")
	suspend fun getByRowId(rowId: Long): TodoEntity?

	@Update(onConflict = OnConflictStrategy.REPLACE)
	suspend fun update(todos: List<TodoEntity>)

	@Query("SELECT * FROM todos WHERE id = :id LIMIT 1")
	suspend fun getTodo(id: Int): TodoEntity?

	@Delete
	suspend fun delete(todos: List<TodoEntity>)
}