package kanti.tododer.data.model.common.datasource.local

import kanti.tododer.data.common.LocalResult

interface TodoLocalDataSource<Todo> where Todo : kanti.tododer.data.model.common.Todo {

	suspend fun getTodo(id: Int): LocalResult<Todo>

	suspend fun getChildren(fid: String): LocalResult<List<Todo>>

	suspend fun insert(vararg todo: Todo): LocalResult<Unit>

	suspend fun insert(todo: Todo): LocalResult<Todo>

	suspend fun update(vararg todo: Todo): LocalResult<Unit>

	suspend fun update(todo: Todo): LocalResult<Todo>

	suspend fun delete(vararg todo: Todo): LocalResult<Unit>

	suspend fun delete(todo: Todo): Boolean

	suspend fun deleteAll()

}