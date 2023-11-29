package kanti.tododer.domain

import kanti.tododer.data.model.plan.FakePlanRepository
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.plan.datasource.local.toPlanEntity
import kanti.tododer.data.model.task.FakeTaskRepository
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.data.model.task.datasource.local.TaskEntity
import kanti.tododer.data.model.task.datasource.local.toTaskEntity
import kanti.tododer.data.model.RepositorySet
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GetParentUseCaseTest {

	private val plans: MutableList<PlanEntity> = mutableListOf()
	private val tasks: MutableList<TaskEntity> = mutableListOf()
	private val repositorySet = RepositorySet(
		taskRepository = FakeTaskRepository(tasks),
		planRepository = FakePlanRepository(plans)
	)
	private val useCase = GetParentUseCase()

	private val planParentId = 1
	private val planParent = PlanImpl(
		id = planParentId
	)
	private val planChildId = 2
	private val planChild = PlanImpl(
		id = planChildId,
		parentId = planParent.fullId
	)
	private val taskChild1Id = 1
	private val taskChild1 = TaskImpl(
		id = taskChild1Id,
		parentId = planParent.fullId
	)
	private val taskChildChild1Id = 2
	private val taskChildChild1 = TaskImpl(
		id = taskChildChild1Id,
		parentId = taskChild1.fullId
	)

	init {
		plans.apply {
			add(planParent.toPlanEntity())
		}
		tasks.apply {
			add(taskChild1.toTaskEntity())
		}
	}

	@Test
	@DisplayName("empty result")
	fun emptyResult() = runTest {
	    val parent = useCase(repositorySet, planParent)
		Assertions.assertNull(parent)
	}

	@Test
	@DisplayName("result  task1")
	fun resultTask1() = runTest {
		val parent = useCase(repositorySet, taskChild1)
		Assertions.assertEquals(planParent, parent)
	}

	@Test
	@DisplayName("result task 2")
	fun resultTask2() = runTest {
		val parent = useCase(repositorySet, taskChildChild1)
		Assertions.assertEquals(taskChild1, parent)
	}

	@Test
	@DisplayName("result plan")
	fun resultPlan() = runTest {
	    val parent = useCase(repositorySet, planChild)
		Assertions.assertEquals(planParent, parent)
	}

}