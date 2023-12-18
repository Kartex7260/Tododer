package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.room.plan.PlanEntity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PlanRoomDataSourceTest {

	private val plans: MutableMap<Int, PlanEntity> = LinkedHashMap()
	private val dataSource: PlanLocalDataSource = PlanRoomDataSource(
		planDao = FakePlanDao(plans),
		initializer = DefaultPlanInitializer()
	)

	@AfterEach
	fun afterEach() = runTest {
		plans.clear()
	}

	@Test
	@DisplayName("standardPlans: Flow<List<Plan>>")
	fun standardPlans() = runTest {
		plans.putAll(
			mapOf(
				1 to PlanEntity(id = 1, archived = false, type = PlanType.All.toString()),
				2 to PlanEntity(id = 2, archived = true, type = PlanType.Custom.toString()),
				3 to PlanEntity(id = 3, archived = false, type = PlanType.Default.toString()),
				4 to PlanEntity(id = 4, archived = true, type = PlanType.Custom.toString())
			)
		)
		val expected = arrayOf(
			Plan(id = 1, archived = false, type = PlanType.All),
			Plan(id = 3, archived = false, type = PlanType.Default)
		)

		launch {
			dataSource.standardPlans.collect {
				assertArrayEquals(
					expected,
					it.toTypedArray()
				)
				cancel("Success")
			}
		}
	}

	@Test
	@DisplayName("archivedPlans: Flow<List<Plan>>")
	fun archivedPlans() = runTest {
		plans.putAll(
			mapOf(
				1 to PlanEntity(id = 1, archived = false, type = PlanType.All.toString()),
				2 to PlanEntity(id = 2, archived = true, type = PlanType.Custom.toString()),
				3 to PlanEntity(id = 3, archived = false, type = PlanType.Default.toString()),
				4 to PlanEntity(id = 4, archived = true, type = PlanType.Custom.toString())
			)
		)
		val expected = arrayOf(
			Plan(id = 2, archived = true, type = PlanType.Custom),
			Plan(id = 4, archived = true, type = PlanType.Custom)
		)

		launch {
			dataSource.archivedPlans.collect {
				assertArrayEquals(
					expected,
					it.toTypedArray()
				)
				cancel("Success")
			}
		}
	}

	@Test
	@DisplayName("insert(Plan) insert 1")
	fun insert1() = runTest {
	    plans.putAll(
			mapOf(
				1 to PlanEntity(id = 1, type = PlanType.Custom.toString()),
				2 to PlanEntity(id = 2, type = PlanType.Custom.toString())
			)
		)
		val expected = Plan(id = 3, title = "Test")

		val plan = dataSource.insert(Plan(title = "Test"))
		assertEquals(
			expected,
			plan
		)
	}

	@Test
	@DisplayName("insert(Plan) insert 2")
	fun insert2() = runTest {
		plans.putAll(
			mapOf(
				1 to PlanEntity(id = 1, type = PlanType.Custom.toString()),
				2 to PlanEntity(id = 2, type = PlanType.Custom.toString())
			)
		)
		val expected = Plan(id = 3, title = "Test")

		val plan = dataSource.insert(Plan(id = 3, title = "Test"))
		assertEquals(
			expected,
			plan
		)
	}

	@Test
	@DisplayName("insert(Plan) insert error")
	fun insertErr() = runTest {
		plans.putAll(
			mapOf(
				1 to PlanEntity(id = 1, type = PlanType.Custom.toString()),
				2 to PlanEntity(id = 2, type = PlanType.Custom.toString())
			)
		)

		try {
			val plan = dataSource.insert(Plan(id = 2, title = "Test"))
			assertNull(plan)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
		}
	}

	@Test
	@DisplayName("update(Plan) update error")
	fun updateError() = runTest {
	    try {
			val plan = dataSource.update(Plan(id = 23))
			assertNull(plan)
		} catch (th: Throwable) {
			assertInstanceOf(IllegalArgumentException::class.java, th)
		}
	}

	@Test
	@DisplayName("update(Plan) update")
	fun update() = runTest {
	    plans.putAll(mapOf(
			1 to PlanEntity(id = 1, title = "Test 1", type = PlanType.Custom.toString()),
			2 to PlanEntity(id = 2, title = "Test 2", type = PlanType.Custom.toString()),
			3 to PlanEntity(id = 3, title = "Test 3", type = PlanType.Custom.toString())
		))
		val expectedPlan = Plan(id = 2, title = "Updated")
		val expectedArray = arrayOf(
			PlanEntity(id = 1, title = "Test 1", type = PlanType.Custom.toString()),
			PlanEntity(id = 2, title = "Updated", type = PlanType.Custom.toString()),
			PlanEntity(id = 3, title = "Test 3", type = PlanType.Custom.toString())
		)

		val plan = dataSource.update(Plan(
			id = 2,
			title = "Updated"
		))
		assertEquals(expectedPlan, plan)
		assertArrayEquals(
			expectedArray,
			plans.values.toTypedArray()
		)
	}

	@Test
	@DisplayName("update(List<Plan>) update empty")
	fun updateListEmpty() = runTest {
		val expected = arrayOf<PlanEntity>()
	    dataSource.update(listOf(
			Plan(id = 1),
			Plan(id = 2),
			Plan(id = 3)
		))

		assertArrayEquals(expected, plans.values.toTypedArray())
	}

	@Test
	@DisplayName("update(List<Plan>) update")
	fun updateList() = runTest {
	    plans.putAll(mapOf(
			1 to PlanEntity(id = 1, title = "Test 1", type = PlanType.All.toString()),
			2 to PlanEntity(id = 2, title = "Test 2", type = PlanType.Default.toString()),
			3 to PlanEntity(id = 3, title = "Test 3", type = PlanType.Custom.toString()),
			4 to PlanEntity(id = 4, title = "Test 4", type = PlanType.Custom.toString())
		))
		val expected = arrayOf(
			PlanEntity(id = 1, title = "Test 1", type = PlanType.All.toString()),
			PlanEntity(id = 2, title = "Updated 1", type = PlanType.Default.toString()),
			PlanEntity(id = 3, title = "Test 3", type = PlanType.Custom.toString()),
			PlanEntity(id = 4, title = "Updated 2", type = PlanType.Custom.toString())
		)

		dataSource.update(listOf(
			Plan(id = 2, title = "Updated 1", type = PlanType.Default),
			Plan(id = 4, title = "Updated 2"),
			Plan(id = 5, title = "Updated 3")
		))

		assertArrayEquals(expected, plans.values.toTypedArray())
	}

	@Test
	@DisplayName("delete(List<Plan>) delete")
	fun delete() = runTest {
		plans.putAll(mapOf(
			1 to PlanEntity(id = 1, title = "Test 1", type = PlanType.All.toString()),
			2 to PlanEntity(id = 2, title = "Test 2", type = PlanType.Default.toString()),
			3 to PlanEntity(id = 3, title = "Test 3", type = PlanType.Custom.toString()),
			4 to PlanEntity(id = 4, title = "Test 4", type = PlanType.Custom.toString())
		))
		val expected = arrayOf(
			PlanEntity(id = 1, title = "Test 1", type = PlanType.All.toString()),
			PlanEntity(id = 2, title = "Test 2", type = PlanType.Default.toString())
		)

		dataSource.delete(listOf(
			Plan(id = 3),
			Plan(id = 4),
			Plan(id = 5)
		))

		assertArrayEquals(expected, plans.values.toTypedArray())
	}

	@Test
	@DisplayName("init()")
	fun init() = runTest {
	    dataSource.init()
		assertNotEquals(0, plans.size)
	}

	@Test
	@DisplayName("isEmpty(): true")
	fun isEmptyTrue() = runTest {
	    val actual = dataSource.isEmpty()
		assertTrue(actual)
	}

	@Test
	@DisplayName("isEmpty(): false")
	fun isEmptyFalse() = runTest {
		plans[1] = PlanEntity(id = 1, type = PlanType.All.toString())
	    val actual = dataSource.isEmpty()
		assertFalse(actual)
	}

	@Test
	@DisplayName("clear()")
	fun clear() = runTest {
		plans.putAll(mapOf(
			1 to PlanEntity(id = 1, title = "Test 1", type = PlanType.All.toString()),
			2 to PlanEntity(id = 2, title = "Test 2", type = PlanType.Default.toString()),
			3 to PlanEntity(id = 3, title = "Test 3", type = PlanType.Custom.toString()),
			4 to PlanEntity(id = 4, title = "Test 4", type = PlanType.Custom.toString())
		))
		dataSource.clear()

		assertNotEquals(0, plans.size)
	}
}