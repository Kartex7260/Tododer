package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.model.todo.toTodo
import kanti.tododer.util.log.Logger

class FakeTodoLocalDataSource(
	private val todosMap: MutableMap<Long, Todo> = LinkedHashMap(),
	private val logger: Logger
) : TodoLocalDataSource {

	override suspend fun getTodo(todoId: Long): Todo? {
		val result = todosMap[todoId]
		logger.d(LOG_TAG, "getTodo(Long = $todoId): return $result")
		return result
	}

	override suspend fun getAllChildren(fullId: FullId): List<Todo> {
		val result = todosMap.values.filter { it.parentId == fullId }
		logger.d(LOG_TAG, "getAllChildren(FullId = $fullId): return count(${result.size})")
		return result
	}

	override suspend fun getChildren(fullId: FullId, state: TodoState?): List<Todo> {
		val filter = if (state == null)
			fun (todo: Todo) = todo.parentId == fullId
		else
			fun (todo: Todo) = todo.parentId == fullId && todo.state == state

		val result = todosMap.values.filter(filter)
		logger.d(
			LOG_TAG,
			"getChildren(FullId = $fullId, TodoState? = $state): return count(${result.size})"
		)
		return result
	}

	override suspend fun getChildrenCount(fullId: FullId, state: TodoState?): Long {
		val filter = if (state == null)
			fun (todo: Todo) = todo.parentId == fullId
		else
			fun (todo: Todo) = todo.parentId == fullId && todo.state == state

		val result = todosMap.values.count(filter).toLong()
		logger.d(
			LOG_TAG,
			"getChildrenCount(FullId = $fullId, TodoState? = $state): return $result"
		)
		return result
	}

	override suspend fun insert(todo: Todo): Long {
		val newTodo = if (todo.id == 0L) {
			val lastId = todosMap.keys.lastOrNull() ?: 0
			todo.toTodo(id = lastId + 1)
		} else {
			if (todosMap.containsKey(todo.id))
				throw IllegalArgumentException("Todo already exist (id = ${todo.id})")
			todo
		}
		todosMap[newTodo.id] = newTodo
		logger.d(LOG_TAG, "insert(Todo = $todo): return ${newTodo.id}")
		return newTodo.id
	}

	override suspend fun updateTitle(todoId: Long, title: String) {
		val todo = todosMap[todoId] ?: return
		val newTodo = todo.toTodo(title = title)
		todosMap[todoId] = newTodo
		logger.d(LOG_TAG, "updateTitle(Long = $todoId, String = $title)")
	}

	override suspend fun updateRemark(todoId: Long, remark: String) {
		val todo = todosMap[todoId] ?: return
		val newTodo = todo.toTodo(remark = remark)
		todosMap[todoId] = newTodo
		logger.d(LOG_TAG, "updateRemark(Long = $todoId, String = $remark)")
	}

	override suspend fun changeDone(todoId: Long, isDone: Boolean) {
		val todo = todosMap[todoId] ?: return
		val newTodo = todo.toTodo(done = isDone)
		todosMap[todoId] = newTodo
		logger.d(LOG_TAG, "changeDone(Long = $todoId, Boolean = $isDone)")
	}

	override suspend fun delete(todoIds: List<Long>) {
		for (todoId in todoIds) {
			todosMap.remove(todoId)
		}
		logger.d(LOG_TAG, "delete(List<Long> = count(${todoIds.size}))")
	}

	override suspend fun deleteIfNameIsEmpty(todoId: Long): Boolean {
		val result = run {
			val todo = todosMap[todoId] ?: return@run false
			if (todo.title.isEmpty()) {
				return@run todosMap.remove(todoId) != null
			}
			return@run false
		}
		logger.d(LOG_TAG, "deleteIfNameIsEmpty(Long = $todoId): return $result")
		return result
	}

	companion object {

		private const val LOG_TAG = "FakeTodoLocalDataSource"
	}
}