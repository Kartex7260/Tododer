package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState

interface TodoLocalDataSource {

	suspend fun getTodo(todoId: Long): Todo?

	suspend fun getAllChildren(fullId: FullId): List<Todo>

	suspend fun getChildren(fullId: FullId, state: TodoState?): List<Todo>

	suspend fun getChildrenCount(fullId: FullId, state: TodoState?): Long

	suspend fun insert(todo: Todo): Long

	suspend fun setGroup(todoIds: List<Long>, group: String?)

	suspend fun ungroup(parent: FullId, group: String)

	suspend fun updateTitle(todoId: Long, title: String)

	suspend fun updateRemark(todoId: Long, remark: String)

	suspend fun changeDone(todoId: Long, isDone: Boolean)

	suspend fun changeDone(todoIds: List<Long>, isDone: Boolean)

	suspend fun changeGroupDone(parent: FullId, group: String?, isDone: Boolean)

	suspend fun delete(todoIds: List<Long>)

	suspend fun deleteIfNameIsEmpty(todoId: Long): Boolean
}