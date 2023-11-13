package kanti.tododer.domain.task

import kanti.tododer.data.common.isAlreadyExists
import kanti.tododer.data.common.isNull
import kanti.tododer.data.model.task.FakeTaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.fullId
import kanti.tododer.domain.removewithchildren.RemoveTaskWithProgenyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoveTaskWithProgenyUseCaseTest {

	private val taskRepository = FakeTaskRepository()
	private val delete1List = mutableListOf<Task>()
	private val delete11List = mutableListOf<Task>()
	private val delete12List = mutableListOf<Task>()
	private val delete1_2List = mutableListOf<Task>()


	private val task1Id: Int = 1
	private val task11Id: Int = 2
	private val task12Id: Int = 3
	private val task111Id: Int = 4
	private val task112Id: Int = 5

	@BeforeEach
	fun setUp() = runTest {
		suspend fun Task.add(): Task {
			val repRes = taskRepository.insert(this)
			println("(${toString()}) repRes: ${repRes.type}")
			return this
		}
		fun Task.add(list: MutableList<Task>): Task {
			list.add(this)
			return this
		}
		val task1 = Task(
			id = task1Id,
			title = "Level 0"
		).add().add(delete11List).add(delete12List)
		val task11 = Task(
			id = task11Id,
			title = "Level 1: 1 (${task1.fullId})",
			parentId = task1.fullId
		).add().add(delete12List)
		val task12 = Task(
			id = task12Id,
			title = "Level 1: 2 (${task1.fullId})",
			parentId = task1.fullId
		).add().add(delete11List)
		val task111 = Task(
			id = task111Id,
			title = "Level 2: 1 (${task11.fullId})",
			parentId = task11.fullId
		).add().add(delete12List)
		val task112 = Task(
			id = task112Id,
			title = "Level 2: 2 (${task11.fullId})",
			parentId = task11.fullId
		).add().add(delete12List)
	}

	@AfterEach
	fun tearDown() = runTest {
		taskRepository.deleteAll()
	}

	@Test
	fun delete1() = runTest {
		val useCase = RemoveTaskWithProgenyUseCase(taskRepository)
		useCase(task1Id)
		Assertions.assertEquals(delete1List, taskRepository.tasks)
	}

	@Test
	fun delete11() = runTest {
		val useCase = RemoveTaskWithProgenyUseCase(taskRepository)
		useCase(task11Id)
		Assertions.assertEquals(delete11List, taskRepository.tasks)
	}

	@Test
	fun delete12() = runTest {
		val useCase = RemoveTaskWithProgenyUseCase(taskRepository)
		useCase(task12Id)
		Assertions.assertEquals(delete12List, taskRepository.tasks)
	}

	@Test
	fun delete1_2() = runTest {
		val repositoryResult = taskRepository.insert(Task(title = "Level 0 2"))
		if (repositoryResult.isAlreadyExists || repositoryResult.isNull)
			Assertions.fail<Task>("Error inserting task")
		delete1_2List.add(repositoryResult.value!!)
		val useCase = RemoveTaskWithProgenyUseCase(taskRepository)
		useCase(task1Id)
		Assertions.assertEquals(delete1_2List, taskRepository.tasks)
	}
}