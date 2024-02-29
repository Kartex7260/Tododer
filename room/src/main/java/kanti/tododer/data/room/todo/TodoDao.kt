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

	@Query("SELECT COUNT(*) FROM todos WHERE parent_id = :parentId")
	suspend fun getAllChildrenCount(parentId: String): Long

	@Query("SELECT * FROM todos WHERE parent_id = :parentId AND state LIKE '%' || :state || '%'")
	suspend fun getChildren(parentId: String, state: String): List<TodoEntity>

	@Query("SELECT COUNT(*) FROM todos WHERE parent_id = :parentId AND state LIKE '%' || :state || '%'")
	suspend fun getChildrenCount(parentId: String, state: String): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(todo: TodoEntity): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(todos: List<TodoEntity>): List<Long>

	@Query("SELECT * FROM todos WHERE rowId = :rowId LIMIT 1")
	suspend fun getByRowId(rowId: Long): TodoEntity?

	@Query("UPDATE todos SET title = :title WHERE rowId IN" +
			"(SELECT rowId FROM todos WHERE id = :todoId LIMIT 1)")
	suspend fun updateTitle(todoId: Long, title: String)

	@Query("UPDATE todos SET remark = :remark WHERE rowId IN" +
			"(SELECT rowId FROM todos WHERE id = :todoId LIMIT 1)")
	suspend fun updateRemark(todoId: Long, remark: String)

	@Query("UPDATE todos SET done = :isDone WHERE rowId IN " +
			"(SELECT rowId FROM todos WHERE id = :todoId LIMIT 1)")
	suspend fun changeDone(todoId: Long, isDone: Boolean)

	@Query("UPDATE todos SET done = :isDone WHERE id IN (:todoIds)")
	suspend fun changeDone(todoIds: List<Long>, isDone: Boolean)

	@Query("SELECT * FROM todos WHERE id = :id LIMIT 1")
	suspend fun getTodo(id: Long): TodoEntity?

	@Query("DELETE FROM todos WHERE id IN(:todoIds)")
	suspend fun delete(todoIds: List<Long>)

	@Query("DELETE FROM todos WHERE id = :todoId AND title = ''")
	suspend fun deleteIfNameIsEmpty(todoId: Long): Int

	@Query("DELETE FROM todos")
	suspend fun deleteAll()
}