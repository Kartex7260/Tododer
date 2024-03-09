package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.datasource.local.FakeTodoLocalDataSource
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TodoRepositoryImplTest {

	private val logger = PrintLogger()

	private val todosMap: MutableMap<Long, Todo> = LinkedHashMap()
	private val dataSource = FakeTodoLocalDataSource(todosMap, logger)
	private val repository: TodoRepository = TodoRepositoryImpl(
		localDataSource = dataSource,
		logger = logger
	)

	@AfterEach
	fun afterEach() = runTest {
		todosMap.clear()
	}

	@Test
	@DisplayName("getTodo(Long)")
	fun getTodo() = runTest {
		val todoId = 1L

		var actual = repository.getTodo(todoId)
		assertNull(actual)

		val expected = Todo(id = todoId)
		todosMap[todoId] = Todo(id = todoId)
		actual = repository.getTodo(todoId)
		assertEquals(expected, actual)
	}

	@Test
	@DisplayName("getChildren(FullId, TodoState?)")
	fun getChildren() = runTest {
		val parentFullId = FullId(id = 1, FullIdType.Plan)
		var actual = repository.getChildren(parentFullId, null)
		assertArrayEquals(arrayOf(), actual.toTypedArray())

		var expected = arrayOf(
			Todo(id = 1L, parentId = parentFullId),
			Todo(id = 2L, parentId = parentFullId),
			Todo(id = 4L, parentId = parentFullId)
		)
		todosMap.putAll(
			mapOf(
				1L to Todo(id = 1L, parentId = parentFullId),
				2L to Todo(id = 2L, parentId = parentFullId),
				3L to Todo(id = 3L, parentId = FullId(4L, FullIdType.Todo)),
				4L to Todo(id = 4L, parentId = parentFullId)
			)
		)
		actual = repository.getChildren(parentFullId, null)
		assertArrayEquals(expected, actual.toTypedArray())

		todosMap.clear()

		actual = repository.getChildren(parentFullId, TodoState.Normal)
		assertArrayEquals(arrayOf(), actual.toTypedArray())

		expected = arrayOf(
			Todo(id = 1L, parentId = parentFullId),
			Todo(id = 2L, parentId = parentFullId),
			Todo(id = 4L, parentId = parentFullId)
		)
		todosMap.putAll(
			mapOf(
				1L to Todo(id = 1L, parentId = parentFullId),
				2L to Todo(id = 2L, parentId = parentFullId),
				3L to Todo(id = 3L, parentId = FullId(4L, FullIdType.Todo)),
				4L to Todo(id = 4L, parentId = parentFullId)
			)
		)
		actual = repository.getChildren(parentFullId, TodoState.Normal)
		assertArrayEquals(expected, actual.toTypedArray())
	}

	@Test
	@DisplayName("getChildrenCount(FullId, TodoState?)")
	fun getChildrenCount() = runTest {
		val parentFullId = FullId(id = 1, FullIdType.Plan)
		var actual = repository.getChildrenCount(parentFullId, null)
		assertEquals(0L, actual)

		todosMap.putAll(
			mapOf(
				1L to Todo(id = 1L, parentId = parentFullId),
				2L to Todo(id = 2L, parentId = parentFullId),
				3L to Todo(id = 3L, parentId = FullId(4L, FullIdType.Todo)),
				4L to Todo(id = 4L, parentId = parentFullId)
			)
		)
		actual = repository.getChildrenCount(parentFullId, null)
		assertEquals(3L, actual)

		todosMap.clear()

		actual = repository.getChildrenCount(parentFullId, TodoState.Normal)
		assertEquals(0L, actual)

		todosMap.putAll(
			mapOf(
				1L to Todo(id = 1L, parentId = parentFullId),
				2L to Todo(id = 2L, parentId = parentFullId),
				3L to Todo(id = 3L, parentId = FullId(4L, FullIdType.Todo)),
				4L to Todo(id = 4L, parentId = parentFullId)
			)
		)
		actual = repository.getChildrenCount(parentFullId, TodoState.Normal)
		assertEquals(3L, actual)
	}

	@Test
	@DisplayName("deleteChildren(FullId)")
	fun deleteChildren() = runTest {
		val parentFullId = FullId(1, FullIdType.Plan)
		repository.deleteChildren(parentFullId)

		val expected = arrayOf(
			Todo(id = 2, parentId = FullId(id = 6, FullIdType.Plan)),
			Todo(id = 6, parentId = FullId(id = 4, FullIdType.Plan))
		)
		todosMap.putAll(
			mapOf(
				1L to Todo(id = 1L, parentId = parentFullId),
				2L to Todo(id = 2L, parentId = FullId(id = 6, FullIdType.Plan)),
				3L to Todo(id = 3L, parentId = parentFullId),
				4L to Todo(id = 4L, parentId = parentFullId),
				5L to Todo(id = 5L, parentId = FullId(id = 3, FullIdType.Todo)),
				6L to Todo(id = 6L, parentId = FullId(id = 4, FullIdType.Plan))
			)
		)
		repository.deleteChildren(parentFullId)
		assertArrayEquals(expected, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("exists(Long)")
	fun exists() = runTest {
		val todoId = 5L
		var actual = repository.exists(todoId)
		assertFalse(actual)

		todosMap[todoId] = Todo(id = todoId)
		actual = repository.exists(todoId)
		assertTrue(actual)
	}

	@Test
	@DisplayName("create(ParentId, String, String)")
	fun create() = runTest {
	    val expectedArray = arrayOf(
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan)),
			Todo(id = 2, parentId = FullId(1, FullIdType.Plan), title = "Test", remark = "Testable")
		)

		todosMap[1] = Todo(id = 1, parentId = FullId(1, FullIdType.Plan))

		val todoId = repository.create(FullId(1, FullIdType.Plan), "Test", "Testable")

		assertEquals(2L, todoId)
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("updateTitle(Todo, String)")
	fun updateTitle() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, title = "Test 1"),
			Todo(id = 2, title = "Updated 1")
		)
	    todosMap.putAll(mapOf(
			1L to Todo(id = 1, title = "Test 1"),
			2L to Todo(id = 2, title = "Test 2")
		))

		repository.updateTitle(todoId = 2, title = "Updated 1")
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())

		repository.updateTitle(todoId = 3, title = "Updated 2")
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("updateRemark(Todo, String)")
	fun updateRemark() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, remark = "Test 1"),
			Todo(id = 2, remark = "Updated 1")
		)
		todosMap.putAll(mapOf(
			1L to Todo(id = 1, remark = "Test 1"),
			2L to Todo(id = 2, remark = "Test 2")
		))

		repository.updateRemark(todoId = 2, remark = "Updated 1")
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())

		repository.updateRemark(todoId = 3, remark = "Updated 2")
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("changeDone(Long, Boolean)")
	fun changeDone() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, done = false),
			Todo(id = 2, done = true)
		)

		todosMap.putAll(mapOf(
			1L to Todo(id = 1, done = true),
			2L to Todo(id = 2, done = false)
		))

		repository.changeDone(todoId = 1, isDone = false)
		repository.changeDone(todoId = 2, isDone = true)
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())

		repository.changeDone(todoId = 3, isDone = true)
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("changeDone(List<Long>, Boolean)")
	fun changeDoneMany() = runTest {
		val expectedArray = arrayOf(
			Todo(id = 1, done = true),
			Todo(id = 2, done = true)
		)

		todosMap.putAll(mapOf(
			1L to Todo(id = 1, done = false),
			2L to Todo(id = 2, done = false)
		))

		repository.changeDone(todoIds = listOf(1, 2), isDone = true)
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())

		repository.changeDone(todoIds = listOf(5), isDone = true)
		assertArrayEquals(expectedArray, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("delete(List<Todo>)")
	fun delete() = runTest {
		val expected = arrayOf(
			Todo(id = 1, parentId = FullId(1, FullIdType.Plan))
		)
		todosMap.putAll(
			mapOf(
				1L to Todo(id = 1, parentId = FullId(1, FullIdType.Plan)),
				2L to Todo(id = 2, parentId = FullId(1, FullIdType.Todo)),
				3L to Todo(id = 3, parentId = FullId(1, FullIdType.Todo)),
				4L to Todo(id = 4, parentId = FullId(3, FullIdType.Todo))
			)
		)

		repository.delete(listOf(2, 3))
		assertArrayEquals(expected, todosMap.values.toTypedArray())

		repository.delete(listOf(6, 8, 2))
		assertArrayEquals(expected, todosMap.values.toTypedArray())
	}

	@Test
	@DisplayName("deleteIfNameIsEmptyAndNoChild(Long)")
	fun deleteIfNameIsEmptyAndNoChild() = runTest {
		val todoId = 1L
		var actual = repository.deleteIfNameIsEmptyAndNoChild(todoId)
		assertFalse(actual)

		todosMap.putAll(
			mapOf(
				1L to Todo(id = todoId)
			)
		)
		actual = repository.deleteIfNameIsEmptyAndNoChild(todoId)
		assertTrue(actual)

		todosMap.putAll(
			mapOf(
				todoId to Todo(id = todoId, title = "Test")
			)
		)
		actual = repository.deleteIfNameIsEmptyAndNoChild(todoId)
		assertFalse(actual)
		assertArrayEquals(
			arrayOf(Todo(id = todoId, title = "Test")),
			todosMap.values.toTypedArray()
		)

		todosMap[todoId] = Todo(id = todoId)
		todosMap[2L] = Todo(id = 2L, parentId = FullId(todoId, FullIdType.Todo))
		actual = repository.deleteIfNameIsEmptyAndNoChild(todoId)
		assertFalse(actual)
		assertArrayEquals(
			arrayOf(
				Todo(id = todoId),
				Todo(id = 2L, parentId = FullId(todoId, FullIdType.Todo))
			),
			todosMap.values.toTypedArray()
		)
	}
}