package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.sl.serialize
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TodoRoomDataSourceTest {

	private val sl = StateLanguage {  }

	private val stateNormal = sl.serialize(TodoState.Normal as TodoState)

	private val todos: MutableMap<Int, TodoEntity> = LinkedHashMap()
	private val dataSource: TodoLocalDataSource = TodoRoomDataSource(
		todoDao = FakeTodoDao(todos),
		sl = sl
	)

	@AfterEach
	fun afterEach() = runTest {
		todos.clear()
	}

	@Test
	@DisplayName("getAllChildren(String)")
	fun getAllChildren() = runTest {
		todos.putAll(mapOf(
			1 to TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal),
			2 to TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal),
			3 to TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal),
			4 to TodoEntity(id = 4, parentId = "Todo-2", state = stateNormal)
		))
		val expected = arrayOf(
			Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
			Todo(id = 3, parentId = FullId(1, FullIdType.Todo))
		)

		val children = dataSource.getAllChildren(FullId(1, FullIdType.Todo))
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	@DisplayName("getChildren(String, TodoState)")
	fun getChildren() = runTest {
	    todos.putAll(mapOf(
			1 to TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal),
			2 to TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal),
			3 to TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal),
			4 to TodoEntity(id = 4, parentId = "Todo-2", state = stateNormal)
		))
		val expected = arrayOf(
			Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
			Todo(id = 3, parentId = FullId(1, FullIdType.Todo))
		)

		val children = dataSource.getChildren(FullId(1, FullIdType.Todo), TodoState.Normal)
		assertArrayEquals(expected, children.toTypedArray())
	}

	@Test
	@DisplayName("insert(Todo) error")
	fun insertError() = runTest {
	    todos[1] = TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal)
		try {
			val todo = dataSource.insert(Todo(id = 1, FullId(1, FullIdType.Plan)))
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
		}
	}

	@Test
	@DisplayName("insert(Todo)")
	fun insert() = runTest {
	    val expectedTodo = Todo(id = 1, parentId = FullId(1, FullIdType.Plan))
		val expectedArray = arrayOf(
			TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal)
		)

		val todo = dataSource.insert(Todo(parentId = FullId(1, FullIdType.Plan)))
		assertEquals(expectedTodo, todo)
		assertArrayEquals(
			expectedArray,
			todos.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("updateTitle(Int, String) error")
	fun updateTitleError() = runTest {
	    try {
			val todo = dataSource.updateTitle(1, "Updated")
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
			assertArrayEquals(arrayOf(), todos.values.toTypedArray())
		}
	}

	@Test
	@DisplayName("updateTitle(Int, String)")
	fun updateTitle() = runTest {
		val expectedArray = arrayOf(
			TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal, title = "Test 1"),
			TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal, title = "Updated 1"),
			TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal, title = "Test 3")
		)
		val expectedTodo = Todo(id = 2, parentId = FullId(1, FullIdType.Todo), title = "Updated 1")

		todos.putAll(mapOf(
			1 to TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal, title = "Test 1"),
			2 to TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal, title = "Test 2"),
			3 to TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal, title = "Test 3")
		))

		val todo = dataSource.updateTitle(2, "Updated 1")

		assertEquals(expectedTodo, todo)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("updateRemark(Int, String) error")
	fun updateRemarkError() = runTest {
		try {
			val todo = dataSource.updateRemark(1, "Updated")
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
			assertArrayEquals(arrayOf(), todos.values.toTypedArray())
		}
	}

	@Test
	@DisplayName("updateRemark(Int, String)")
	fun updateRemark() = runTest {
		val expectedArray = arrayOf(
			TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal, remark = "Test 1"),
			TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal, remark = "Updated 1"),
			TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal, remark = "Test 3")
		)
		val expectedTodo = Todo(id = 2, parentId = FullId(1, FullIdType.Todo), remark = "Updated 1")

		todos.putAll(mapOf(
			1 to TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal, remark = "Test 1"),
			2 to TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal, remark = "Test 2"),
			3 to TodoEntity(id = 3, parentId = "Todo-1", state = stateNormal, remark = "Test 3")
		))

		val todo = dataSource.updateRemark(2, "Updated 1")

		assertEquals(expectedTodo, todo)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("changeDone(Int) error")
	fun changeDoneError() = runTest {
		try {
			val todo = dataSource.changeDone(1)
			assertNull(todo)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
			assertArrayEquals(arrayOf(), todos.values.toTypedArray())
		}
	}

	@Test
	@DisplayName("changeDone(Int)")
	fun changeDone() = runTest {
		val expectedArray = arrayOf(
			TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal, done = false),
			TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal, done = true)
		)
		val expectedTodo1 = Todo(id = 1, parentId = FullId(1, FullIdType.Plan), done = false)
		val expectedTodo2 = Todo(id = 2, parentId = FullId(1, FullIdType.Todo), done = true)

		todos.putAll(mapOf(
			1 to TodoEntity(id = 1, parentId = "Plan-1", state = stateNormal, done = true),
			2 to TodoEntity(id = 2, parentId = "Todo-1", state = stateNormal, done = false)
		))

		val todo1 = dataSource.changeDone(1)
		val todo2 = dataSource.changeDone(2)

		assertEquals(expectedTodo1, todo1)
		assertEquals(expectedTodo2, todo2)
		assertArrayEquals(expectedArray, todos.values.toTypedArray())
	}

	@Test
	@DisplayName("delete(List<Todo>)")
	fun deleteList() = runTest {
		todos.putAll(mapOf(
			1 to TodoEntity(id = 1, title = "Test 1", parentId = "Plan-1", state = stateNormal),
			2 to TodoEntity(id = 2, title = "Test 2", parentId = "Todo-1", state = stateNormal),
			3 to TodoEntity(id = 3, title = "Test 3", parentId = "Todo-1", state = stateNormal),
			4 to TodoEntity(id = 4, title = "Test 4", parentId = "Todo-3", state = stateNormal)
		))
		val expected = arrayOf(
			TodoEntity(id = 1, title = "Test 1", parentId = "Plan-1", state = stateNormal),
			TodoEntity(id = 3, title = "Test 3", parentId = "Todo-1", state = stateNormal)
		)

		dataSource.delete(listOf(2, 4, 5))

		assertArrayEquals(expected, todos.values.toTypedArray())
	}
}