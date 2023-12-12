package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.model.common.result.isNotFound
import kanti.tododer.data.model.common.result.isSuccess
import kanti.tododer.data.model.task.Task
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TaskRoomDataSourceTest {

	private val tasks: MutableList<kanti.tododer.data.model.task.datasource.local.TaskEntity> = mutableListOf()
	private val dataSource: TaskLocalDataSource = FakeTaskRoomDataSource(tasks)

	private val parentId = "root"

	@AfterEach
	fun afterEach() = runTest {
		tasks.clear()
	}

	//<editor-fold desc="getTask(Int)">
	@Test
	@DisplayName("getTask(Int): Not found 1")
	fun getTaskNotFound1() = runTest {
		val localResult = dataSource.getTask(1)
		assertTrue(localResult.isNotFound)
	}

	@Test
	@DisplayName("getTask(Int): Not found 2")
	fun getTaskNotFound2() = runTest {
		val localResult = dataSource.getTask(-1)
		assertTrue(localResult.isNotFound)
	}

	@Test
	@DisplayName("getTask(Int): found")
	fun getTaskFound() = runTest {
		tasks.add(
			kanti.tododer.data.model.task.datasource.local.TaskEntity(
				id = 1,
				title = "Test"
			)
		)
		val localResult = dataSource.getTask(1)
		assertTrue(localResult.isSuccess)
		assertEquals(
			Task(
				id = 1,
				title = "Test"
			),
			(localResult as GetLocalResult.Success).value
		)
	}
	//</editor-fold>

	//<editor-fold desc="getChildren(String)"
	@Test
	@DisplayName("getChildren(String): empty result 1")
	fun getChildrenEmpty1() = runTest {
	    val children = dataSource.getChildren(parentId)
		assertTrue(children.isSuccess)
		assertArrayEquals(
			arrayOf<Task>(),
			children.getOrNull()?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): empty result 2")
	fun getChildrenEmpty2() = runTest {
		val children = dataSource.getChildren("")
		assertTrue(children.isSuccess)
		assertArrayEquals(
			arrayOf<Task>(),
			children.getOrNull()?.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 1")
	fun getChildren1() = runTest {
		tasks.addAll(
			arrayOf(
				kanti.tododer.data.model.task.datasource.local.TaskEntity(
					id = 1,
					parentId = parentId
				),
				kanti.tododer.data.model.task.datasource.local.TaskEntity(
					id = 2,
					parentId = parentId
				),
				kanti.tododer.data.model.task.datasource.local.TaskEntity(
					id = 3,
					parentId = "missParentId"
				)
			)
		)

		val children = dataSource.getChildren(parentId)
		assertTrue(children.isSuccess)
		assertArrayEquals(
			arrayOf(
				Task(
					id = 1,
					parentId = parentId
				),
				Task(
					id = 2,
					parentId = parentId
				)
			),
			children.getOrNull()?.toTypedArray()
		)
	}
	//</editor-fold>

	//<editor-fold desc="insert(Task)"
	//</editor-fold>
}