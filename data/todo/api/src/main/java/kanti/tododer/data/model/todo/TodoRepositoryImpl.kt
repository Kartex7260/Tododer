package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
	private val localDataSource: TodoLocalDataSource
) : TodoRepository {

	override suspend fun getTodo(todoId: Long): Todo? {
		return localDataSource.getTodo(todoId)
	}

	override suspend fun getChildren(fullId: FullId, state: TodoState?): List<Todo> {
		return localDataSource.getChildren(fullId, state)
	}

	override suspend fun getChildrenCount(fullId: FullId, state: TodoState?): Long {
		return localDataSource.getChildrenCount(fullId, state)
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
		for (todoId in todoIds) {
			val fullId = FullId(todoId, FullIdType.Todo)
			val children = getChildren(fullId)
			delete(children.map { it.id })
		}
		localDataSource.delete(todoIds)
	}

	override suspend fun deleteIfNameIsEmptyAndNoChild(todoId: Long): Boolean {
		val fullId = FullId(todoId, FullIdType.Todo)
		val childrenCount = localDataSource.getChildrenCount(fullId, null)
		if (childrenCount != 0L)
			return false
		return localDataSource.deleteIfNameIsEmpty(todoId)
	}
}