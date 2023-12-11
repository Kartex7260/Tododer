package kanti.tododer.data.model.task.datasource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kanti.tododer.TododerApplication
import kanti.tododer.data.room.TododerDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class TaskDaoTest {

	private val db: TododerDatabase
	private val taskDao: TaskDao

	private val parentId = "root"

	init {
		val context = ApplicationProvider.getApplicationContext<TododerApplication>()
		db = Room.inMemoryDatabaseBuilder(context, TododerDatabase::class.java).build()
		taskDao = db.taskDao()
	}

	@After
	fun afterEach() = runTest {
		taskDao.deleteAll()
	}

	@Test
	fun getAll1() = runTest {
		val all = taskDao.getAll()
		assertArrayEquals(
			arrayOf(),
			all.toTypedArray()
		)
	}

	@Test
	fun getAll2() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
			),
			TaskEntity(
				id = 6
			),
			TaskEntity(
				id = 135
			)
		)
		val all = taskDao.getAll()
		assertArrayEquals(
			arrayOf(
				TaskEntity(
					id = 1,
				),
				TaskEntity(
					id = 6
				),
				TaskEntity(
					id = 135
				)
			),
			all.toTypedArray()
		)
	}

	@Test
	fun getChildren1() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Mis1",
				parentId = "plan-144151"
			),
			TaskEntity(
				id = 2,
				title = "Suc1",
				parentId = parentId
			),
			TaskEntity(
				id = 3,
				title = "Suc2",
				parentId = parentId
			),
			TaskEntity(
				id = 4,
				title = "Mis2",
				parentId = "task-5135513"
			)
		)

		val children = taskDao.getChildren(parentId)
		assertArrayEquals(
			arrayOf(
				TaskEntity(
					id = 2,
					title = "Suc1",
					parentId = parentId
				),
				TaskEntity(
					id = 3,
					title = "Suc2",
					parentId = parentId
				)
			),
			children.toTypedArray()
		)
	}

	@Test
	fun getChildren2() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Mis1",
				parentId = "plan-144151"
			),
			TaskEntity(
				id = 2,
				title = "Mis2",
				parentId = "task-5135513"
			)
		)

		val children = taskDao.getChildren(parentId)
		assertArrayEquals(
			arrayOf(),
			children.toTypedArray()
		)
	}

	@Test
	fun getByRowId1() = runTest {
		val rowIds = taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Test 1"
			),
			TaskEntity(
				id = 2,
				title = "Test 2"
			)
		)

		val task = taskDao.getByRowId(rowIds[1])
		assertEquals(
			TaskEntity(
				id = 2,
				title = "Test 2"
			),
			task
		)
	}

	@Test
	fun getByRowId2() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Test 1"
			),
			TaskEntity(
				id = 2,
				title = "Test 2"
			)
		)

		val task = taskDao.getByRowId(1801825028)
		assertNull(task)
	}

	@Test
	fun getTask1() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Test 1"
			),
			TaskEntity(
				id = 2,
				title = "Test 2"
			)
		)

		val actual = taskDao.getTask(2)
		assertEquals(
			TaskEntity(
				id = 2,
				title = "Test 2"
			),
			actual
		)
	}

	@Test
	fun getTask2() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Test 1"
			),
			TaskEntity(
				id = 2,
				title = "Test 2"
			)
		)

		val actual = taskDao.getTask(3)
		assertNull(actual)
	}

	@Test
	fun insert1() = runTest {
		val rowIds = taskDao.insert(
			TaskEntity(
				title = "Task1",
				parentId = parentId
			),
			TaskEntity(
				title = "Task2",
				parentId = parentId
			),
			TaskEntity(
				title = "Task3",
				parentId = parentId
			)
		)
		assertArrayEquals(
			arrayOf<Long>(
				1, 2, 3
			),
			rowIds
		)
		assertArrayEquals(
			arrayOf(
				TaskEntity(
					id = 1,
					title = "Task1",
					parentId = parentId
				),
				TaskEntity(
					id = 2,
					title = "Task2",
					parentId = parentId
				),
				TaskEntity(
					id = 3,
					title = "Task3",
					parentId = parentId
				)
			),
			taskDao.getAll().toTypedArray()
		)
	}

	@Test
	fun insert2() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 3,
				title = "Task3",
				parentId = parentId
			)
		)
		val rowIds = taskDao.insert(
			TaskEntity(
				id = 1,
				title = "Task1",
				parentId = parentId
			),
			TaskEntity(
				id = 2,
				title = "Task2",
				parentId = parentId
			),
			TaskEntity(
				id = 3,
				title = "AnotherTask3",
				parentId = parentId
			)
		)
		assertArrayEquals(
			arrayOf<Long>(
				1, 2, 3
			),
			rowIds
		)
		assertArrayEquals(
			arrayOf(
				TaskEntity(
					id = 1,
					title = "Task1",
					parentId = parentId
				),
				TaskEntity(
					id = 2,
					title = "Task2",
					parentId = parentId
				),
				TaskEntity(
					id = 3,
					title = "AnotherTask3",
					parentId = parentId
				)
			),
			taskDao.getAll().toTypedArray()
		)
	}

	@Test
	fun delete1() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
			),
			TaskEntity(
				id = 2
			),
			TaskEntity(
				id = 3
			)
		)
		val deletedCount = taskDao.delete(
			TaskEntity(
				id = 2
			)
		)

		assertEquals(1, deletedCount)
		assertArrayEquals(
			arrayOf(
				TaskEntity(
					id = 1
				),
				TaskEntity(
					id = 3
				)
			),
			taskDao.getAll().toTypedArray()
		)
	}

	@Test
	fun delete2() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
			),
			TaskEntity(
				id = 2
			),
			TaskEntity(
				id = 3
			)
		)
		val deletedCount = taskDao.delete(
			TaskEntity(
				id = 2
			),
			TaskEntity(
				id = 4
			)
		)

		assertEquals(1, deletedCount)
		assertArrayEquals(
			arrayOf(
				TaskEntity(
					id = 1
				),
				TaskEntity(
					id = 3
				)
			),
			taskDao.getAll().toTypedArray()
		)
	}

	@Test
	fun deleteAll() = runTest {
		taskDao.insert(
			TaskEntity(
				id = 1,
				parentId = parentId
			),
			TaskEntity(
				id = 2,
				parentId = parentId
			),
			TaskEntity(
				id = 3,
				parentId = parentId
			)
		)
		taskDao.deleteAll()
		val children = taskDao.getChildren(parentId)
		assertArrayEquals(
			arrayOf(),
			children.toTypedArray()
		)
	}
}