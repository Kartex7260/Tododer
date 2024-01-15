package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
	private val localDataSource: TodoLocalDataSource
) : TodoRepository {

	override suspend fun getChildren(fullId: FullId): List<Todo> {
		return localDataSource.getChildren(fullId, TodoState.Normal)
	}

	override suspend fun deleteChildren(fullId: FullId) {
		val children = localDataSource.getAllChildren(fullId)
		localDataSource.delete(children.map { it.id })
	}

	override suspend fun create(
		parentFullId: FullId,
		title: String,
		remark: String
	): Long {
		val todo = Todo(
			parentId = parentFullId,
			title = title,
			remark = remark
		)
		return localDataSource.insert(todo)
	}

	override suspend fun updateTitle(todoId: Long, title: String) {
		localDataSource.updateTitle(todoId, title)
	}

	override suspend fun updateRemark(todoId: Long, remark: String) {
		localDataSource.updateRemark(todoId, remark)
	}

	override suspend fun changeDone(todoId: Long, isDone: Boolean) {
		localDataSource.changeDone(todoId, isDone)
	}

	override suspend fun delete(todoIds: List<Long>) {
		localDataSource.delete(todoIds)
	}
}