package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.data.room.todo.TodoEntity
import kanti.tododer.util.log.Logger

class FakeTodoDao(
	private val todosMap: MutableMap<Long, TodoEntity> = LinkedHashMap(),
	private val logger: Logger
) : TodoDao {

	override suspend fun getAll(): List<TodoEntity> {
		val result = todosMap.values.toList()
		logger.d(LOG_TAG, "getAll(): return count(${result.size})")
		return result
	}

	override suspend fun getAllChildren(parentId: String): List<TodoEntity> {
		val result = todosMap.values.filter { it.parentId == parentId }
		logger.d(LOG_TAG, "getAllChildren(String = $parentId): return count(${result.size})")
		return result
	}

	override suspend fun getAllChildrenCount(parentId: String): Long {
		val result = todosMap.values.count { it.parentId == parentId }.toLong()
		logger.d(LOG_TAG, "getAllChildrenCount(String = $parentId): return $result")
		return result
	}

	override suspend fun getChildren(parentId: String, state: String): List<TodoEntity> {
		val result = todosMap.values.filter { it.parentId == parentId && it.state.contains(state) }
		logger.d(
			LOG_TAG,
			"getChildren(String = $parentId, String = $state): return count(${result.size})"
		)
		return result
	}

	override suspend fun getChildrenCount(parentId: String, state: String): Long {
		val result = todosMap.values.count { it.parentId == parentId && it.state.contains(state) }
		logger.d(
			LOG_TAG,
			"getChildrenCount(String = $parentId, String = $state): return $result"
		)
		return result.toLong()
	}

	override suspend fun insert(todo: TodoEntity): Long {
		val newTodo = if (todo.id == 0L) {
			val lastId = todosMap.keys.lastOrNull() ?: 0
			todo.copy(id = lastId + 1)
		} else {
			if (todosMap.containsKey(todo.id))
				return -1
			todo
		}
		todosMap[newTodo.id] = newTodo
		logger.d(LOG_TAG, "insert(TodoEntity = $todo): return ${newTodo.id}")
		return newTodo.id
	}

	override suspend fun insert(todos: List<TodoEntity>): List<Long> {
		logger.enabled(false)
		val result = mutableListOf<Long>()
		for (todo in todos) {
			val todoRowId = insert(todo)
			result.add(todoRowId)
		}
		logger.enabled(true)
		val addedCount = result.count { it != -1L }
		logger.d(LOG_TAG, "insert(List<TodoEntity> = count(${result.size})): adding result " +
				"${result.size / addedCount}")
		return result
	}

	override suspend fun getByRowId(rowId: Long): TodoEntity? {
		val result = todosMap[rowId]
		logger.d(LOG_TAG, "getByRowId(Long = $rowId): return $result")
		return result
	}

	override suspend fun updateTitle(todoId: Long, title: String) {
		val todo = todosMap[todoId]
		logger.d(
			LOG_TAG,
			"updateTitle(Long = $todoId, String = $title): changed = ${todo != null}"
		)
		if (todo == null)
			return
		val newTodo = todo.copy(title = title)
		todosMap[todoId] = newTodo
	}

	override suspend fun updateRemark(todoId: Long, remark: String) {
		val todo = todosMap[todoId]
		logger.d(
			LOG_TAG,
			"updateRemark(Long = $todoId, String = $remark): changed = ${todo != null}"
		)
		if (todo == null)
			return
		val newTodo = todo.copy(remark = remark)
		todosMap[todoId] = newTodo
	}

	override suspend fun changeDone(todoId: Long, isDone: Boolean) {
		val todo = todosMap[todoId]
		logger.d(
			LOG_TAG,
			"changeDone(Long = $todoId, Boolean = $isDone): changed = ${todo != null}"
		)
		if (todo == null)
			return
		val newTodo = todo.copy(done = !todo.done)
		todosMap[todoId] = newTodo
	}

	override suspend fun getTodo(id: Long): TodoEntity? {
		val todo = todosMap[id]
		logger.d(LOG_TAG, "getTodo(Long = $id): return $todo")
		return todo
	}

	override suspend fun delete(todoIds: List<Long>) {
		for (todoId in todoIds) {
			todosMap.remove(todoId)
		}
		logger.d(LOG_TAG, "delete(List<Long> = count(${todoIds.size}))")
	}

	override suspend fun deleteIfNameIsEmpty(todoId: Long): Int {
		val todo = todosMap[todoId]
		val emptyTitle = todo?.title?.isEmpty() ?: false
		val deleted = if (emptyTitle) {
			todosMap.remove(todoId) != null
		} else false
		logger.d(LOG_TAG, "deleteIfNameIsEmpty(Long = $todoId): return $deleted")
		return if (deleted) 1 else 0
	}

	override suspend fun deleteAll() {
		todosMap.clear()
		logger.d(LOG_TAG, "deleteAll()")
	}

	companion object {

		private const val LOG_TAG = "FakeTodoDao"
	}
}