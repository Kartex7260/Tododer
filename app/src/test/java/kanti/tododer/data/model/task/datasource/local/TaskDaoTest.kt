package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.toTask
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TaskDaoTest {

	private val tasks = mutableListOf<TaskEntity>()
	private val dao: BaseTaskDao = FakeTaskDao(tasks)
	private val tasksArray: Array<Task>
		get() = tasks.map { it.toTask() }.toTypedArray()

	private val parentId = "root"

	private val id1 = 1
	private val task1 = TaskImpl(
		id = id1,
		parentId = parentId
	)
	private val id2 = 2
	private val task2 = TaskImpl(
		id = id2,
		parentId = "foo"
	)
	private val id3 = 3
	private val task3 = TaskImpl(
		id = id3,
		parentId = parentId
	)

	@AfterEach
	fun after() {
		tasks.clear()
	}

	@Test
	@DisplayName("getChildren(String): empty result 1")
	fun getChildrenEmpty1() = runTest {
		val children = dao.getChildren(parentId)
		Assertions.assertArrayEquals(
			arrayOf(),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): empty result 2")
	fun getChildrenEmpty2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val children = dao.getChildren("wrong_parentId")
		Assertions.assertArrayEquals(
			arrayOf(),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 1")
	fun getChildrenResult1() = runTest {
		tasks.add(task1.toTaskEntity())
		val children = dao.getChildren(parentId)
		Assertions.assertArrayEquals(
			arrayOf(task1),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 2")
	fun getChildrenResult2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val children = dao.getChildren(parentId)
		Assertions.assertArrayEquals(
			arrayOf(task1, task3),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getByRowId(Long): result null 1")
	fun getByRowIdResultNull1() = runTest {
		val task = dao.getByRowId(1L)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getByRowId(Long): result null 2")
	fun getByRowIdResultNull2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val task = dao.getByRowId(10L)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getByRowId(Long): result 1")
	fun getByRowIdResult1() = runTest {
		tasks.add(task1.toTaskEntity())
		val task = dao.getByRowId(1L)
		Assertions.assertEquals(task1, task)
	}

	@Test
	@DisplayName("getByRowId(Long): result 2")
	fun getByRowIdResult2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val task = dao.getByRowId(2L)
		Assertions.assertEquals(task2, task)
	}

	@Test
	@DisplayName("getTodo(Int): result null 1")
	fun getTodoResultNull1() = runTest {
		val task = dao.getTodo(id1)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getTodo(Int): result null 2")
	fun getTodoResultNull2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val task = dao.getTodo(5342)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getTodo(Int): result 1")
	fun getTodoResult1() = runTest {
		tasks.add(task1.toTaskEntity())
		val task = dao.getTodo(id1)
		Assertions.assertEquals(task1, task)
	}

	@Test
	@DisplayName("getTodo(Int): result 2")
	fun getTodoResult2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val task = dao.getTodo(id2)
		Assertions.assertEquals(task2, task)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 1")
	fun insertVararg1() = runTest {
		dao.insert(*arrayOf(task1))
		Assertions.assertArrayEquals(
			arrayOf(task1),
			tasksArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 2")
	fun insertVararg2() = runTest {
		dao.insert(task1, task2)
		Assertions.assertArrayEquals(
			arrayOf(task1, task2),
			tasksArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 3")
	fun insertVararg3() = runTest {
		dao.insert(task1, task2)
		dao.insert(task2, task3)
		Assertions.assertArrayEquals(
			arrayOf(task1, task2, task3),
			tasksArray
		)
	}

	@Test
	@DisplayName("insert(Todo): insert 1")
	fun insert1() = runTest {
		val rowId = dao.insert(task1)
		Assertions.assertEquals(1L, rowId)
	}

	@Test
	@DisplayName("insert(Todo): insert 2")
	fun insert2() = runTest {
		val rowId1 = dao.insert(task1)
		val rowId2 = dao.insert(task2)
		Assertions.assertEquals(1L, rowId1)
		Assertions.assertEquals(2L, rowId2)
	}

	@Test
	@DisplayName("insert(Todo): insert 3")
	fun insert3() = runTest {
		val rowId1 = dao.insert(task1)
		val rowId2 = dao.insert(task2)
		val rowId3 = dao.insert(task1)
		Assertions.assertEquals(1L, rowId1)
		Assertions.assertEquals(2L, rowId2)
		Assertions.assertEquals(-1L, rowId3)
	}

	@Test
	@DisplayName("update(vararg Todo): fail update 1")
	fun updateVarargFail1() = runTest {
		dao.update(*arrayOf(task1))
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): fail update 2")
	fun updateVarargFail2() = runTest {
		tasks.apply {
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		dao.update(*arrayOf(task1))
		Assertions.assertArrayEquals(
			arrayOf(task2, task3),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 1")
	fun updateVarargUpdate1() = runTest {
		tasks.add(task1.toTaskEntity())
		dao.update(*arrayOf(task1.toTask(title = "test")))
		Assertions.assertArrayEquals(
			arrayOf(task1.toTask(title = "test")),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 2")
	fun updateVarargUpdate2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
		}
		dao.update(*arrayOf(task1.toTask(title = "test")))
		Assertions.assertArrayEquals(
			arrayOf(task1.toTask(title = "test"), task2),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 3")
	fun updateVarargUpdate3() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		dao.update(*arrayOf(
			task1.toTask(title = "test"),
			task3.toTask(remark = "foo")
		))
		Assertions.assertArrayEquals(
			arrayOf(
				task1.toTask(title = "test"),
				task2,
				task3.toTask(remark = "foo")),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(Todo): fail update 1")
	fun updateFail1() = runTest {
		val updateResult = dao.update(task1)
		Assertions.assertFalse(updateResult)
	}

	@Test
	@DisplayName("update(Todo): fail update 2")
	fun updateFail2() = runTest {
		tasks.add(task2.toTaskEntity())
		val updateResult = dao.update(task1)
		Assertions.assertFalse(updateResult)
		Assertions.assertArrayEquals(
			arrayOf(task2),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(Todo): update 1")
	fun update1() = runTest {
		tasks.add(task1.toTaskEntity())
		val updateResult = dao.update(task1.toTask(title = "Test"))
		Assertions.assertTrue(updateResult)
		Assertions.assertArrayEquals(
			arrayOf(task1.toTask(title = "Test")),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(Todo): update 2")
	fun update2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val updateResult = dao.update(task2.toTask(remark = "Test"))
		Assertions.assertTrue(updateResult)
		Assertions.assertArrayEquals(
			arrayOf(
				task1,
				task2.toTask(remark = "Test"),
				task3
			),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): fail delete 1")
	fun deleteVarargFailDelete1() = runTest {
		dao.delete(*arrayOf(task1))
	}

	@Test
	@DisplayName("delete(vararg Todo): fail delete 2")
	fun deleteVarargFailDelete2() = runTest {
		tasks.add(task1.toTaskEntity())
		dao.delete(*arrayOf(task2))
		Assertions.assertArrayEquals(
			arrayOf(task1),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 1")
	fun deleteVararg1() = runTest {
		tasks.add(task1.toTaskEntity())
		dao.delete(*arrayOf(task1))
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 2")
	fun deleteVararg2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
		}
		dao.delete(task1, task2)
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 3")
	fun deleteVararg3() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		dao.delete(task1, task3)
		Assertions.assertArrayEquals(
			arrayOf(task2),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(Todo): fail delete 1")
	fun deleteFail1() = runTest {
		val deleteResult = dao.delete(task1)
		Assertions.assertFalse(deleteResult)
	}

	@Test
	@DisplayName("delete(Todo): fail delete 2")
	fun deleteFail2() = runTest {
		tasks.add(task2.toTaskEntity())
		val deleteResult = dao.delete(task1)
		Assertions.assertFalse(deleteResult)
		Assertions.assertArrayEquals(
			arrayOf(task2),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(Todo): delete 1")
	fun delete1() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
		}
		val deleteResult = dao.delete(task1)
		Assertions.assertTrue(deleteResult)
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(Todo): delete 2")
	fun delete2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		val deleteResult = dao.delete(task2)
		Assertions.assertTrue(deleteResult)
		Assertions.assertArrayEquals(
			arrayOf(task1, task3),
			tasksArray
		)
	}

	@Test
	@DisplayName("deleteAll(): 1")
	fun deleteAll1() = runTest {
		dao.deleteAll()
	}

	@Test
	@DisplayName("deleteAll(): 2")
	fun deleteAll2() = runTest {
		tasks.apply {
			add(task1.toTaskEntity())
			add(task2.toTaskEntity())
			add(task3.toTaskEntity())
		}
		dao.deleteAll()
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

}