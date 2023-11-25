package kanti.tododer.data.model.common

import kanti.tododer.data.common.RepositoryResult

interface TodoRepository<Todo> where Todo : kanti.tododer.data.model.common.Todo {

	suspend fun getTodo(id: Int): RepositoryResult<Todo>

	suspend fun getChildren(fid: String): RepositoryResult<List<Todo>>

	suspend fun insert(vararg todo: Todo): RepositoryResult<Unit>

	suspend fun insert(task: Todo): RepositoryResult<Todo>

	suspend fun update(vararg todo: Todo): RepositoryResult<Unit>

	suspend fun update(
		todo: Todo,
		update: (Todo.() -> Todo)? = null
	): RepositoryResult<Todo>

	suspend fun delete(vararg task: Todo): RepositoryResult<Unit>

	suspend fun delete(task: Todo): Boolean

	suspend fun deleteAll()

}