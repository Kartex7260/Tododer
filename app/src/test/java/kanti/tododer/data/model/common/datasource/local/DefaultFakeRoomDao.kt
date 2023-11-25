package kanti.tododer.data.model.common.datasource.local

import kanti.tododer.data.model.common.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DefaultFakeRoomDao<T>(
	private val newId: T.(id: Int) -> T,
	val todos: MutableList<T> = mutableListOf()
) where T : Todo {

	suspend fun getChildrenRoom(parentId: String): List<T> {
		return async {
			todos.filter { it.parentId == parentId }
		}
	}

	suspend fun getByRowIdRoom(rowId: Long): T? {
		return async {
			if (rowId < 1)
				throw IllegalArgumentException("rowId cannot be less than 0")
			todos.getOrNull(rowId.toInt() - 1)
		}
	}

	suspend fun getTodoRoom(id: Int): T? {
		return async {
			todos.firstOrNull { it.id == id }
		}
	}

	suspend fun insertRoom(vararg todo: T): Array<Long> {
		val rowIds = mutableListOf<Long>()
		fun addTodo(id: Int, todo: T): Int {
			if (containsTask(todo)) {
				rowIds.add(-1)
				return id
			}
			return if (todo.id == 0) {
				val newId = id + 1
				todos.add(todo.newId(newId))
				rowIds.add(todos.indexOfFirst { it.id == newId }.toLong() + 1)
				newId
			} else {
				todos.add(todo)
				rowIds.add(todos.indexOfFirst { it.id == todo.id }.toLong() + 1)
				todo.id
			}
		}

		launch {
			var lastId = todos.lastOrNull()?.id ?: 0
			for (td in todo) {
				lastId = addTodo(lastId, td)
			}
		}
		return rowIds.toTypedArray()
	}

	suspend fun updateRoom(vararg todo: T): Int {
		return async {
			var result = 0
			for (tsk in todo) {
				val index = todos.indexOfFirst { it.id == tsk.id }
				if (index == -1) {
					continue
				}
				todos[index] = tsk
				result++
			}
			result
		}
	}

	suspend fun deleteRoom(vararg todo: T): Int {
		return async {
			var result = 0
			for (tsk in todo) {
				val removeResult = todos.removeIf { it.id == tsk.id }
				if (removeResult)
					result++
			}
			result
		}
	}

	suspend fun deleteAllRoom() {
		launch {
			todos.clear()
		}
	}

	private fun containsTask(task: T): Boolean {
		if (task.id == 0)
			return false
		return todos.firstOrNull { it.id == task.id } != null
	}

	private suspend fun <T> async(block: suspend CoroutineScope.() -> T): T {
		return coroutineScope {
			async(
				context = Dispatchers.Default,
				block = block
			).await()
		}
	}

	private suspend fun launch(block: suspend CoroutineScope.() -> Unit) {
		return coroutineScope {
			launch(
				context = Dispatchers.Default,
				block = block
			)
		}
	}

}