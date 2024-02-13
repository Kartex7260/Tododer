package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRoomDataSource @Inject constructor(
	private val todoDao: TodoDao,
	private val sl: StateLanguage,
	@StandardLog private val logger: Logger
) : TodoLocalDataSource {

	override suspend fun getTodo(todoId: Long): Todo? {
		val result = withContext(Dispatchers.IO) { todoDao.getTodo(todoId)?.toTodo(sl) }
		logger.d(LOG_TAG, "getTodo(Long = $todoId): return $result")
		return result
	}

	override suspend fun getAllChildren(fullId: FullId): List<Todo> {
		val result = withContext(Dispatchers.IO) {
			todoDao.getAllChildren(fullId.toString()).map { it.toTodo(sl) }
		}
		logger.d(LOG_TAG, "getAllChildren(FullId = $fullId): return count(${result.size})")
		return result
	}

	override suspend fun getChildren(fullId: FullId, state: TodoState?): List<Todo> {
		val result = withContext(Dispatchers.IO) {
			if (state == null) {
				return@withContext getAllChildren(fullId)
			}
			todoDao.getChildren(fullId.toString(), state.name).map { it.toTodo(sl) }
		}
		logger.d(
			LOG_TAG,
			"getChildren(FullId = $fullId, TodoState? = $state): return count(${result.size})"
		)
		return result
	}

	override suspend fun getChildrenCount(fullId: FullId, state: TodoState?): Long {
		val result = withContext(Dispatchers.IO) {
			if (state == null) {
				return@withContext todoDao.getAllChildrenCount(fullId.toString())
			}
			todoDao.getChildrenCount(fullId.toString(), state.name)
		}
		logger.d(
			LOG_TAG,
			"getChildrenCount(FullId = $fullId, TodoState? = $state): return $result"
		)
		return result
	}

	override suspend fun insert(todo: Todo): Long {
		val result =  withContext(Dispatchers.IO) { todoDao.insert(todo.toTodoEntity(sl)) }
		logger.d(LOG_TAG, "insert(Todo = $todo): return $result")
		return result
	}

	override suspend fun updateTitle(todoId: Long, title: String) {
		todoDao.updateTitle(todoId, title)
		logger.d(LOG_TAG, "updateTitle(Long = $todoId, String = $title)")
	}

	override suspend fun updateRemark(todoId: Long, remark: String) {
		todoDao.updateRemark(todoId, remark)
		logger.d(LOG_TAG, "updateRemark(Long = $todoId, String = $remark)")
	}

	override suspend fun changeDone(todoId: Long, isDone: Boolean) {
		todoDao.changeDone(todoId, isDone)
		logger.d(LOG_TAG, "changeDone(Long = $todoId, Boolean = $isDone)")
	}

	override suspend fun delete(todoIds: List<Long>) {
		todoDao.delete(todoIds)
		logger.d(LOG_TAG, "delete(List<Long> = count(${todoIds.size}))")
	}

	override suspend fun deleteIfNameIsEmpty(todoId: Long): Boolean {
		val result = todoDao.deleteIfNameIsEmpty(todoId) == 1
		logger.d(LOG_TAG, "deleteIfNameIsEmpty(Long = $todoId): return $result")
		return result
	}

	companion object {

		private const val LOG_TAG = "TodoRoomDataSource"
	}
}