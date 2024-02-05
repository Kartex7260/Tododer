package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRoomDataSource @Inject constructor(
	private val todoDao: TodoDao,
	private val sl: StateLanguage
) : TodoLocalDataSource {

	override suspend fun getTodo(todoId: Long): Todo? {
		return withContext(Dispatchers.IO) {
			todoDao.getTodo(todoId)?.toTodo(sl)
		}
	}

	override suspend fun getAllChildren(fullId: FullId): List<Todo> {
		return withContext(Dispatchers.IO) {
			todoDao.getAllChildren(fullId.toString()).map { it.toTodo(sl) }
		}
	}

	override suspend fun getChildren(fullId: FullId, state: TodoState?): List<Todo> {
		return withContext(Dispatchers.IO) {
			if (state == null) {
				return@withContext getAllChildren(fullId)
			}
			todoDao.getChildren(fullId.toString(), state.name).map { it.toTodo(sl) }
		}
	}

	override suspend fun getChildrenCount(fullId: FullId, state: TodoState?): Long {
		return withContext(Dispatchers.IO) {
			if (state == null) {
				return@withContext todoDao.getAllChildrenCount(fullId.toString())
			}
			todoDao.getChildrenCount(fullId.toString(), state.name)
		}
	}

	override suspend fun insert(todo: Todo): Long {
		return withContext(Dispatchers.IO) {
			val rowId = todoDao.insert(todo.toTodoEntity(sl))
			if (rowId == -1L)
				throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
			rowId
		}
	}

	override suspend fun updateTitle(todoId: Long, title: String) {
		todoDao.updateTitle(todoId, title)
	}

	override suspend fun updateRemark(todoId: Long, remark: String) {
		todoDao.updateRemark(todoId, remark)
	}

	override suspend fun changeDone(todoId: Long, isDone: Boolean) {
		todoDao.changeDone(todoId, isDone)
	}

	override suspend fun delete(todoIds: List<Long>) {
		todoDao.delete(todoIds)
	}

	override suspend fun deleteIfNameIsEmpty(todoId: Long): Boolean {
		return todoDao.deleteIfNameIsEmpty(todoId) == 1
	}
}