package kanti.tododer.data.model.common.datasource.local

interface TodoDao<Todo> where Todo : kanti.tododer.data.model.common.Todo {

	suspend fun getChildren(parentId: String): List<Todo>

	suspend fun getByRowId(rowId: Long): Todo?

	suspend fun getTodo(id: Int): Todo?

	suspend fun insert(vararg todo: Todo)

	suspend fun insert(todo: Todo): Long

	suspend fun update(vararg todo: Todo)

	suspend fun update(todo: Todo): Boolean

	suspend fun delete(vararg todo: Todo)

	suspend fun delete(todo: Todo): Boolean

	suspend fun deleteAll()

}