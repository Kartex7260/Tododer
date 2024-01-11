package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.FakePlanLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PlanRepositoryImplTest {

	private val plans: MutableMap<Int, Plan> = LinkedHashMap()
	private val repository: PlanRepository = PlanRepositoryImpl(FakePlanLocalDataSource(
		planInitializer = DefaultPlanInitializer(),
		plans = plans
	))

	@AfterEach
	fun afterEach() = runTest {
		plans.clear()
	}

	@Test
	@DisplayName("create(String)")
	fun create() = runTest {
		val expectedPlan = Plan(id = 1, title = "Test")
	    val plan = repository.create("Test")

		val expectedArray = arrayOf(
			Plan(id = 1, title = "Test")
		)

		assertEquals(expectedPlan, plan)
		assertArrayEquals(expectedArray, plans.values.toTypedArray())
	}

	@Test
	@DisplayName("updateTitle(Plan, String)")
	fun updateTitle() = runTest {
		val expectedPlan = Plan(id = 1, title = "Updated")
		val expectedArray = arrayOf(Plan(id = 1, title = "Updated"))
	    plans.putAll(mapOf(
			1 to Plan(id = 1, title = "Test 1")
		))

		val plan = repository.updateTitle(1, title = "Updated")

		assertEquals(expectedPlan, plan)
		assertArrayEquals(expectedArray, plans.values.toTypedArray())
	}

	@Test
	@DisplayName("delete(List<Plan>)")
	fun delete() = runTest {
	    plans.putAll(mapOf(
			1 to Plan(id = 1),
			2 to Plan(id = 2),
			3 to Plan(id = 3)
		))
		val expectedArray = arrayOf(Plan(id = 2))

		repository.delete(listOf(1, 3, 4))
		assertArrayEquals(expectedArray, plans.values.toTypedArray())
	}

	@Test
	@DisplayName("init()")
	fun init() = runTest {
	    repository.init()
		assertNotEquals(0, plans.size)
	}

	@Test
	@DisplayName("isEmpty() true")
	fun isEmptyTrue() = runTest {
	    val actual = repository.isEmpty()
		assertTrue(actual)
	}

	@Test
	@DisplayName("isEmpty() false")
	fun isEmptyFalse() = runTest {
	    plans[1] = Plan(id = 1)
		val actual = repository.isEmpty()
		assertFalse(actual)
	}

	@Test
	@DisplayName("clear()")
	fun clear() = runTest {
	    plans.putAll(mapOf(
			1 to Plan(id = 1),
			2 to Plan(id = 2),
			3 to Plan(id = 3)
		))

		repository.clear()
		val actual = repository.isEmpty()
		assertFalse(actual)
	}
}