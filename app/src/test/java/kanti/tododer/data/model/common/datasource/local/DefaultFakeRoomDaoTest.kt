package kanti.tododer.data.model.common.datasource.local

import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.toTask
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DefaultFakeRoomDaoTest {

	private val parentId = "root"

	private val dao = DefaultFakeRoomDao<Task>(
		newId = { newId ->
			toTask(
				id = newId
			)
		}
	)

	private val emptyArray = arrayOf<Task>()

	private val id1 = 1
	private val task1 = TaskImpl(
		id = id1,
		parentId = parentId
	)
	private val id2 = 2
	private val task2 = TaskImpl(
		id = id2,
		parentId = "test"
	)
	private val id3 = 3
	private val task3 = TaskImpl(
		id = id3,
		parentId = parentId
	)

	@BeforeEach
	fun before() = runTest {
		dao.deleteAllRoom()
	}

	@Test
	@DisplayName("getChildrenRoom(String): Empty result")
	fun getChildrenRoomEmpty() = runTest {
		val children = dao.getChildrenRoom(parentId)
		Assertions.assertArrayEquals(
			emptyArray,
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildrenRoom(String): Return 1")
	fun getChildrenRoomOneChild() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}
		val children = dao.getChildrenRoom(parentId)
		Assertions.assertArrayEquals(
			arrayOf(task1),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildrenRoom(String): Return all")
	fun getChildrenRoomAllChild() = runTest {
		dao.todos.apply {
			add(task1)
			add(task3)
		}
		val children = dao.getChildrenRoom(parentId)
		Assertions.assertArrayEquals(
			arrayOf(task1, task3),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getByRowIdRoom(Long): Return null")
	fun getByRowIdRoomNull() = runTest {
		val task = dao.getByRowIdRoom(0L)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getByRowIdRoom(Long): return task 1")
	fun getByRowIdRoomTask1() = runTest {
		dao.todos.apply {
			add(task1)
		}
		val task = dao.getByRowIdRoom(0L)
		Assertions.assertEquals(task1, task)
	}

	@Test
	@DisplayName("getByRowIdRoom(Long): return task 2")
	fun getByRowIdRoomTask2() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}
		val task = dao.getByRowIdRoom(1L)
		Assertions.assertEquals(task2, task)
	}

	@Test
	@DisplayName("getTodoRoom(Int): return null")
	fun getTodoRoomNull() = runTest {
		val task = dao.getTodoRoom(id1)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getTodoRoom(Int): return task 1")
	fun getTodoRoom1() = runTest {
		dao.todos.apply {
			add(task1)
		}
		val task = dao.getTodoRoom(id1)
		Assertions.assertEquals(task1, task)
	}

	@Test
	@DisplayName("getTodoRoom(Int): return task 2")
	fun getTodoRoom2() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}
		val task = dao.getTodoRoom(id2)
		Assertions.assertEquals(task2, task)
	}

	@Test
	@DisplayName("insertRoom(vararg Todo): insert todo")
	fun insertRoomTodo() = runTest {
		val rowIds = dao.insertRoom(task1)
		Assertions.assertArrayEquals(
			arrayOf(task1),
			dao.todos.toTypedArray()
		)
		Assertions.assertArrayEquals(
			arrayOf(0L),
			rowIds
		)
	}

	@Test
	@DisplayName("insertRoom(vararg Todo): insert todos 1")
	fun insertRoomTodos1() = runTest {
		val rowIds = dao.insertRoom(task1, task2)
		Assertions.assertArrayEquals(
			arrayOf(task1, task2),
			dao.todos.toTypedArray()
		)
		Assertions.assertArrayEquals(
			arrayOf(0L, 1L),
			rowIds
		)
	}

	@Test
	@DisplayName("insertRoom(vararg Todo): insert todos 2")
	fun insertRoomTodos2() = runTest {
		val rowIds = dao.insertRoom(task1, task2, TaskImpl())
		Assertions.assertArrayEquals(
			arrayOf(task1, task2, TaskImpl(id = 3)),
			dao.todos.toTypedArray()
		)
		Assertions.assertArrayEquals(
			arrayOf(0L, 1L, 2L),
			rowIds
		)
	}

	@Test
	@DisplayName("insertRoom(vararg Todo): double insert ignore")
	fun insertRoomDoubleIgnore() = runTest {
		val rowIds1 = dao.insertRoom(task1, task2)
		val rowIds2 = dao.insertRoom(task2, task3)
		Assertions.assertArrayEquals(
			arrayOf(task1, task2, task3),
			dao.todos.toTypedArray()
		)
		Assertions.assertArrayEquals(
			arrayOf(0L, 1L),
			rowIds1
		)
		Assertions.assertArrayEquals(
			arrayOf(2L),
			rowIds2
		)
	}

	@Test
	@DisplayName("updateRoom(vararg Todo): update no exist 1")
	fun updateRoomNoExist1() = runTest {
		val updatedCount = dao.updateRoom(task1)
		Assertions.assertArrayEquals(
			arrayOf<Task>(),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(0, updatedCount)
	}

	@Test
	@DisplayName("updateRoom(vararg Todo): update no exist 2")
	fun updateRoomNoExist2() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}
		val updatedCount = dao.updateRoom(task3)
		Assertions.assertArrayEquals(
			arrayOf<Task>(task1, task2),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(0, updatedCount)
	}

	@Test
	@DisplayName("updateRoom(vararg Todo): update exist 1")
	fun updateRoomExist1() = runTest {
		dao.todos.apply {
			add(task1)
		}
		val updatedCount = dao.updateRoom(task1.toTask(title = "Test"))
		Assertions.assertArrayEquals(
			arrayOf(task1.toTask(title = "Test")),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(1, updatedCount)
	}

	@Test
	@DisplayName("updateRoom(vararg Todo): update exist 2")
	fun updateRoomExist2() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}
		val updatedCount = dao.updateRoom(
			task1.toTask(title = "Test"),
			task2.toTask(remark = "Foo")
		)

		Assertions.assertArrayEquals(
			arrayOf(
				task1.toTask(title = "Test"),
				task2.toTask(remark = "Foo")
			),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(2, updatedCount)
	}

	@Test
	@DisplayName("updateRoom(vararg Todo): update exist 3")
	fun updateRoomExist3() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
			add(task3)
		}
		val updatedCount = dao.updateRoom(
			task1.toTask(title = "Test"),
			task2.toTask(remark = "Foo")
		)

		Assertions.assertArrayEquals(
			arrayOf(
				task1.toTask(title = "Test"),
				task2.toTask(remark = "Foo"),
				task3
			),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(2, updatedCount)
	}

	@Test
	@DisplayName("deleteRoom(vararg Todo): delete no exist 1")
	fun deleteRoomNoExist1() = runTest {
		val deletedCount = dao.deleteRoom(task1)
		Assertions.assertEquals(0, deletedCount)
	}

	@Test
	@DisplayName("deleteRoom(vararg Todo): delete no exist 2")
	fun deleteRoomNoExist2() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}

		val deletedCount = dao.deleteRoom(task3)

		Assertions.assertArrayEquals(
			arrayOf(task1, task2),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(0, deletedCount)
	}

	@Test
	@DisplayName("deleteRoom(vararg Todo): delete exist 1")
	fun deleteRoomExist1() = runTest {
		dao.todos.add(task1)
		val deletedCount = dao.deleteRoom(task1)
		Assertions.assertArrayEquals(
			arrayOf(),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(1, deletedCount)
	}

	@Test
	@DisplayName("deleteRoom(vararg Todo): delete exist 2")
	fun deleteRoomExist2() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
		}
		val deletedCount = dao.deleteRoom(task1)
		Assertions.assertArrayEquals(
			arrayOf(task2),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(1, deletedCount)
	}

	@Test
	@DisplayName("deleteRoom(vararg Todo): delete exist 3")
	fun deleteRoomExist3() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
			add(task3)
		}
		val deletedCount = dao.deleteRoom(task1, task3)
		Assertions.assertArrayEquals(
			arrayOf(task2),
			dao.todos.toTypedArray()
		)
		Assertions.assertEquals(2, deletedCount)
	}

	@Test
	@DisplayName("deleteAllRoom() 1")
	fun deleteAllRoom1() = runTest {
		dao.deleteAllRoom()
	}

	@Test
	@DisplayName("deleteAllRoom() 2")
	fun deleteAllRoom2() = runTest {
		dao.todos.add(task1)
		dao.deleteAllRoom()
		Assertions.assertArrayEquals(
			arrayOf(),
			dao.todos.toTypedArray()
		)
	}

	@Test
	@DisplayName("deleteAllRoom() 3")
	fun deleteAllRoom3() = runTest {
		dao.todos.apply {
			add(task1)
			add(task2)
			add(task3)
		}
		dao.deleteAllRoom()
		Assertions.assertArrayEquals(
			arrayOf(),
			dao.todos.toTypedArray()
		)
	}

}