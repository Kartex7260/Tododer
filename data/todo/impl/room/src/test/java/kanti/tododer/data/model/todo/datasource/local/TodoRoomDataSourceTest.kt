package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoState
import kanti.tododer.data.room.todo.TodoEntity
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TodoRoomDataSourceTest {

	private val logTag = "TodoRoomDataSourceTest"
	private val logger = PrintLogger()

	private val sl = StateLanguage {  }

	private val todosMap: MutableMap<Long, TodoEntity> = LinkedHashMap()
	private val dao = FakeTodoDao(todosMap, logger)
	private val dataSource: TodoLocalDataSource = TodoRoomDataSource(
		todoDao = dao,
		sl = sl,
		logger = logger
	)

	@AfterEach
	fun afterEach() = runTest {
		todosMap.clear()
		logger.d(logTag, "----------- AFTER EACH -----------")
	}

	@Test
	@DisplayName("getTodo(Long)")
	fun getTodo() = runTest {
		val todoId = 1L
		var actual = dataSource.getTodo(todoId)
		assertNull(actual)

		todosMap[todoId] = Todo(id = todoId).toTodoEntity(sl)
		actual = dataSource.getTodo(todoId)
		assertEquals(Todo(id = todoId), actual)
	}

	@Test
	@DisplayName("getAllChildren(FullId)")
	fun getAllChildren() = runTest {
		val fullId = FullId(1, FullIdType.Plan)
		var expected = arrayOf<Todo>()
		var actual = dataSource.getAllChildren(fullId)
		assertArrayEquals(expected, actual.toTypedArray())

		todosMap.putAll(
			mapOf(
				1L to Todo(1, parentId = fullId).toTodoEntity(sl),
				2L to Todo(2, parentId = fullId).toTodoEntity(sl),
				3L to Todo(3, parentId = fullId).toTodoEntity(sl)
			)
		)
		expected = arrayOf(
			Todo(1, parentId = fullId),
			Todo(2, parentId = fullId),
			Todo(3, parentId = fullId)
		)
		actual = dataSource.getAllChildren(fullId)
		assertArrayEquals(expected, actual.toTypedArray())
	}

	@Test
	@DisplayName("getChildren(FullId, TodoState?)")
	fun getChildren() = runTest {
		val fullId = FullId(1, FullIdType.Plan)
		var expected: Array<Todo> = arrayOf()
		var actual = dataSource.getChildren(fullId, null)
		assertArrayEquals(expected, actual.toTypedArray())

		todosMap.putAll(
			mapOf(
				1L to Todo(1, parentId = fullId).toTodoEntity(sl),
				2L to Todo(2, parentId = fullId).toTodoEntity(sl)
			)
		)
		expected = arrayOf(
			Todo(1, parentId = fullId),
			Todo(2, parentId = fullId)
		)
		actual = dataSource.getChildren(fullId, null)
		assertArrayEquals(expected, actual.toTypedArray())

		todosMap.clear()

		val state: TodoState = TodoState.Normal
		expected = arrayOf()
		actual = dataSource.getChildren(fullId, state)
		assertArrayEquals(expected, actual.toTypedArray())

		todosMap.putAll(
			mapOf(
				1L to Todo(1, parentId = fullId).toTodoEntity(sl),
				2L to Todo(2, parentId = fullId).toTodoEntity(sl)
			)
		)
		expected = arrayOf(
			Todo(1, parentId = fullId),
			Todo(2, parentId = fullId)
		)
		actual = dataSource.getChildren(fullId, state)
		assertArrayEquals(expected, actual.toTypedArray())
	}

	@Test
	@DisplayName("getChildrenCount(FullId, TodoState?)")
	fun getChildrenCount() = runTest {
		val fullId = FullId(1, FullIdType.Plan)
		var expected: Long = 0
		var actual = dataSource.getChildrenCount(fullId, null)
		assertEquals(expected, actual)

		todosMap.putAll(
			mapOf(
				1L to Todo(1, parentId = fullId).toTodoEntity(sl),
				2L to Todo(2, parentId = fullId).toTodoEntity(sl)
			)
		)
		expected = 2
		actual = dataSource.getChildrenCount(fullId, null)
		assertEquals(expected, actual)

		todosMap.clear()

		val state: TodoState = TodoState.Normal
		expected = 0
		actual = dataSource.getChildrenCount(fullId, state)
		assertEquals(expected, actual)

		todosMap.putAll(
			mapOf(
				1L to Todo(1, parentId = fullId).toTodoEntity(sl),
				2L to Todo(2, parentId = fullId).toTodoEntity(sl)
			)
		)
		expected = 2
		actual = dataSource.getChildrenCount(fullId, state)
		assertEquals(expected, actual)
	}

	@Test
	@DisplayName("insert(Todo)")
	fun insert() = runTest {
		var actual = dataSource.insert(Todo(0))
		assertEquals(1L, actual)
		assertArrayEquals(
			arrayOf(Todo(1).toTodoEntity(sl)),
			todosMap.values.toTypedArray()
		)

		actual = dataSource.insert(Todo(1, title = "Test"))
		assertEquals(-1L, actual)
		assertArrayEquals(
			arrayOf(Todo(1).toTodoEntity(sl)),
			todosMap.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("updateTitle(Long, String)")
	fun updateTitle() = runTest {
		dataSource.updateTitle(1L, "Test 1")
		assertEquals(0, todosMap.size)

		todosMap[1] = Todo(1).toTodoEntity(sl)
		dataSource.updateTitle(1L, "Test 1")
		assertArrayEquals(
			arrayOf(Todo(1, title = "Test 1").toTodoEntity(sl)),
			todosMap.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("updateRemark(Long, String)")
	fun updateRemark() = runTest {
		dataSource.updateRemark(1L, "Test 1")
		assertEquals(0, todosMap.size)

		todosMap[1] = Todo(1).toTodoEntity(sl)
		dataSource.updateRemark(1L, "Test 1")
		assertArrayEquals(
			arrayOf(Todo(1, remark = "Test 1").toTodoEntity(sl)),
			todosMap.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("changeDone(Long, Boolean)")
	fun changeDone() = runTest {
		dataSource.changeDone(1L, true)
		assertEquals(0, todosMap.size)

		todosMap[1] = Todo(1).toTodoEntity(sl)
		dataSource.changeDone(1L, true)
		assertArrayEquals(
			arrayOf(Todo(1, done = true).toTodoEntity(sl)),
			todosMap.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("changeDone(List<Long>, Boolean)")
	fun changeDoneMany() = runTest {
		dataSource.changeDone(1L, true)
		assertEquals(0, todosMap.size)

		todosMap[1] = Todo(1).toTodoEntity(sl)
		todosMap[2] = Todo(2).toTodoEntity(sl)
		dataSource.changeDone(listOf(1, 2), true)
		assertArrayEquals(
			arrayOf(
				Todo(1, done = true).toTodoEntity(sl),
				Todo(2, done = true).toTodoEntity(sl)
			),
			todosMap.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("delete(List<Long>)")
	fun delete() = runTest {
		dataSource.delete(listOf(2, 3))

		todosMap.putAll(
			mapOf(
				1L to Todo(1).toTodoEntity(sl),
				2L to Todo(2).toTodoEntity(sl),
				3L to Todo(3).toTodoEntity(sl)
			)
		)
		val expected = arrayOf(
			Todo(1).toTodoEntity(sl)
		)
		dataSource.delete(listOf(2, 3))
		assertArrayEquals(expected, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("deleteIfNameIsEmpty(Long)")
	fun deleteIfNameIsEmpty() = runTest {
		val todoId: Long = 1
		var actual = dataSource.deleteIfNameIsEmpty(todoId)
		assertFalse(actual)

		todosMap[todoId] = Todo(todoId).toTodoEntity(sl)
		actual = dataSource.deleteIfNameIsEmpty(todoId)
		assertTrue(actual)
		assertArrayEquals(arrayOf(), todosMap.values.toTypedArray())

		todosMap[todoId] = Todo(todoId, title = "Test").toTodoEntity(sl)
		actual = dataSource.deleteIfNameIsEmpty(todoId)
		assertFalse(actual)
		assertArrayEquals(
			arrayOf(Todo(todoId, title = "Test").toTodoEntity(sl)),
			todosMap.values.toTypedArray()
		)
	}
}