package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoDao
import javax.inject.Inject

class TodoRoomDataSource @Inject constructor(
	private val todoDao: TodoDao,
	private val sl: StateLanguage
) : TodoLocalDataSource {

	override suspend fun getAllChildren(fullId: FullId): List<Todo> {
		return todoDao.getAllChildren(fullId.toString()).map { it.toTodo(sl) }
	}

	override suspend fun getChildren(fullId: FullId, state: TodoState): List<Todo> {
		return todoDao.getChildren(fullId.toString(), state.name).map { it.toTodo(sl) }
	}

	override suspend fun insert(todo: Todo): Long {
		val rowId = todoDao.insert(todo.toTodoEntity(sl))
		if (rowId == -1L)
			throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
		return rowId
	}

	override suspend fun updateTitle(todoId: Long, title: String) {
		todoDao.updateTitle(todoId, title)
	}

	override suspend fun updateRemark(todoId: Long, remark: String) {
		todoDao.updateRemark(todoId, remark)
	}

	override suspend fun changeDone(todoId: Long) {
		todoDao.changeDone(todoId)
	}

	override suspend fun delete(todoIds: List<Long>) {
		for (todoId in todoIds) {
			val children = getAllChildren(FullId(todoId, FullIdType.Todo))
			delete(children.map { it.id })
		}
		todoDao.delete(todoIds)
	}
}