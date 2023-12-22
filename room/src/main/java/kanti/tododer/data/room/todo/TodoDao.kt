package kanti.tododer.data.room.todo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {

	@Query("SELECT * FROM todos")
	suspend fun getAll(): List<TodoEntity>

	@Query("SELECT * FROM todos WHERE parent_id = :parentId")
	suspend fun getAllChildren(parentId: String): List<TodoEntity>

	@Query("SELECT * FROM todos WHERE parent_id = :parentId AND state LIKE '%' || :state || '%'")
	suspend fun getChildren(parentId: String, state: String): List<TodoEntity>

	@Query("DELETE FROM todos WHERE parent_id = :parentId")
	suspend fun deleteChildren(parentId: String)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(todo: TodoEntity): Long

	@Query("SELECT * FROM todos WHERE rowId = :rowId LIMIT 1")
	suspend fun getByRowId(rowId: Long): TodoEntity?

	@Query("UPDATE todos SET title = :title WHERE rowId IN" +
			"(SELECT rowId FROM todos WHERE id = :todoId LIMIT 1)")
	suspend fun updateTitle(todoId: Int, title: String)

	@Query("UPDATE todos SET remark = :remark WHERE rowId IN" +
			"(SELECT rowId FROM todos WHERE id = :todoId LIMIT 1)")
	suspend fun updateRemark(todoId: Int, remark: String)

	@Query("UPDATE todos SET done = NOT done WHERE rowId IN " +
			"(SELECT rowId FROM todos WHERE id = :todoId LIMIT 1)")
	suspend fun changeDone(todoId: Int)

	@Query("SELECT * FROM todos WHERE id = :id LIMIT 1")
	suspend fun getTodo(id: Int): TodoEntity?

	@Query("DELETE FROM todos WHERE id IN(:todoIds)")
	suspend fun delete(todoIds: List<Int>)

	@Query("DELETE FROM todos")
	suspend fun deleteAll()
}