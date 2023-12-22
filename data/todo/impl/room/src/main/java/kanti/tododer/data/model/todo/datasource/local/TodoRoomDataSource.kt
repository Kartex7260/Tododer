package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoDao
import javax.inject.Inject

class TodoRoomDataSource @Inject constructor(
	private val todoDao: TodoDao,
	private val sl: StateLanguage
) : TodoLocalDataSource {

	override suspend fun getAllChildren(parentId: ParentId): List<Todo> {
		return todoDao.getAllChildren(parentId.toString()).map { it.toTodo(sl) }
	}

	override suspend fun getChildren(parentId: ParentId, state: TodoState): List<Todo> {
		return todoDao.getChildren(parentId.toString(), state.name).map { it.toTodo(sl) }
	}

	override suspend fun insert(todo: Todo): Todo {
		val rowId = todoDao.insert(todo.toTodoEntity(sl))
		if (rowId == -1L)
			throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
		return todoDao.getByRowId(rowId)?.toTodo(sl)
			?: throw IllegalStateException("Not found todo by rowId=$rowId")
	}

	override suspend fun updateTitle(todoId: Int, title: String): Todo {
		todoDao.updateTitle(todoId, title)
		return requireTodo(todoId)
	}

	override suspend fun updateRemark(todoId: Int, remark: String): Todo {
		todoDao.updateRemark(todoId, remark)
		return requireTodo(todoId)
	}

	override suspend fun changeDone(todoId: Int): Todo {
		todoDao.changeDone(todoId)
		return requireTodo(todoId)
	}

	override suspend fun delete(todoIds: List<Int>) {
		for (todoId in todoIds) {
			val todo = todoDao.getTodo(todoId) ?: continue
			val children = getAllChildren(todo.toParentId())
			delete(children.map { it.id })
		}
		todoDao.delete(todoIds)
	}

	private suspend fun requireTodo(todoId: Int): Todo {
		return todoDao.getTodo(todoId)?.toTodo(sl)
			?: throw IllegalArgumentException("Not found todo by id=$todoId")
	}
}