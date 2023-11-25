package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.plan.toPlan
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PlanDaoTest {

	private val plans = mutableListOf<PlanEntity>()
	private val dao: BasePlanDao = FakePlanDao(plans)
	private val tasksArray: Array<Plan>
		get() = plans.map { it.toPlan() }.toTypedArray()

	private val parentId = "root"

	private val id1 = 1
	private val plan1 = PlanImpl(
		id = id1,
		parentId = parentId
	)
	private val id2 = 2
	private val plan2 = PlanImpl(
		id = id2,
		parentId = "foo"
	)
	private val id3 = 3
	private val plan3 = PlanImpl(
		id = id3,
		parentId = parentId
	)

	@AfterEach
	fun after() {
		plans.clear()
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
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
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
		plans.add(plan1.toPlanEntity())
		val children = dao.getChildren(parentId)
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			children.toTypedArray()
		)
	}

	@Test
	@DisplayName("getChildren(String): result 2")
	fun getChildrenResult2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val children = dao.getChildren(parentId)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan3),
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
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val task = dao.getByRowId(10L)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getByRowId(Long): result 1")
	fun getByRowIdResult1() = runTest {
		plans.add(plan1.toPlanEntity())
		val task = dao.getByRowId(1L)
		Assertions.assertEquals(plan1, task)
	}

	@Test
	@DisplayName("getByRowId(Long): result 2")
	fun getByRowIdResult2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val task = dao.getByRowId(2L)
		Assertions.assertEquals(plan2, task)
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
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val task = dao.getTodo(5342)
		Assertions.assertNull(task)
	}

	@Test
	@DisplayName("getTodo(Int): result 1")
	fun getTodoResult1() = runTest {
		plans.add(plan1.toPlanEntity())
		val task = dao.getTodo(id1)
		Assertions.assertEquals(plan1, task)
	}

	@Test
	@DisplayName("getTodo(Int): result 2")
	fun getTodoResult2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val task = dao.getTodo(id2)
		Assertions.assertEquals(plan2, task)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 1")
	fun insertVararg1() = runTest {
		dao.insert(*arrayOf(plan1))
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			tasksArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 2")
	fun insertVararg2() = runTest {
		dao.insert(plan1, plan2)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan2),
			tasksArray
		)
	}

	@Test
	@DisplayName("insert(vararg Todo): insert 3")
	fun insertVararg3() = runTest {
		dao.insert(plan1, plan2)
		dao.insert(plan2, plan3)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan2, plan3),
			tasksArray
		)
	}

	@Test
	@DisplayName("insert(Todo): insert 1")
	fun insert1() = runTest {
		val rowId = dao.insert(plan1)
		Assertions.assertEquals(1L, rowId)
	}

	@Test
	@DisplayName("insert(Todo): insert 2")
	fun insert2() = runTest {
		val rowId1 = dao.insert(plan1)
		val rowId2 = dao.insert(plan2)
		Assertions.assertEquals(1L, rowId1)
		Assertions.assertEquals(2L, rowId2)
	}

	@Test
	@DisplayName("insert(Todo): insert 3")
	fun insert3() = runTest {
		val rowId1 = dao.insert(plan1)
		val rowId2 = dao.insert(plan2)
		val rowId3 = dao.insert(plan1)
		Assertions.assertEquals(1L, rowId1)
		Assertions.assertEquals(2L, rowId2)
		Assertions.assertEquals(-1L, rowId3)
	}

	@Test
	@DisplayName("update(vararg Todo): fail update 1")
	fun updateVarargFail1() = runTest {
		dao.update(*arrayOf(plan1))
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): fail update 2")
	fun updateVarargFail2() = runTest {
		plans.apply {
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		dao.update(*arrayOf(plan1))
		Assertions.assertArrayEquals(
			arrayOf(plan2, plan3),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 1")
	fun updateVarargUpdate1() = runTest {
		plans.add(plan1.toPlanEntity())
		dao.update(*arrayOf(plan1.toPlan(title = "test")))
		Assertions.assertArrayEquals(
			arrayOf(plan1.toPlan(title = "test")),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 2")
	fun updateVarargUpdate2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
		}
		dao.update(*arrayOf(plan1.toPlan(title = "test")))
		Assertions.assertArrayEquals(
			arrayOf(plan1.toPlan(title = "test"), plan2),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(vararg Todo): update 3")
	fun updateVarargUpdate3() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		dao.update(*arrayOf(
			plan1.toPlan(title = "test"),
			plan3.toPlan(remark = "foo")
		))
		Assertions.assertArrayEquals(
			arrayOf(
				plan1.toPlan(title = "test"),
				plan2,
				plan3.toPlan(remark = "foo")),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(Todo): fail update 1")
	fun updateFail1() = runTest {
		val updateResult = dao.update(plan1)
		Assertions.assertFalse(updateResult)
	}

	@Test
	@DisplayName("update(Todo): fail update 2")
	fun updateFail2() = runTest {
		plans.add(plan2.toPlanEntity())
		val updateResult = dao.update(plan1)
		Assertions.assertFalse(updateResult)
		Assertions.assertArrayEquals(
			arrayOf(plan2),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(Todo): update 1")
	fun update1() = runTest {
		plans.add(plan1.toPlanEntity())
		val updateResult = dao.update(plan1.toPlan(title = "Test"))
		Assertions.assertTrue(updateResult)
		Assertions.assertArrayEquals(
			arrayOf(plan1.toPlan(title = "Test")),
			tasksArray
		)
	}

	@Test
	@DisplayName("update(Todo): update 2")
	fun update2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val updateResult = dao.update(plan2.toPlan(remark = "Test"))
		Assertions.assertTrue(updateResult)
		Assertions.assertArrayEquals(
			arrayOf(
				plan1,
				plan2.toPlan(remark = "Test"),
				plan3
			),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): fail delete 1")
	fun deleteVarargFailDelete1() = runTest {
		dao.delete(*arrayOf(plan1))
	}

	@Test
	@DisplayName("delete(vararg Todo): fail delete 2")
	fun deleteVarargFailDelete2() = runTest {
		plans.add(plan1.toPlanEntity())
		dao.delete(*arrayOf(plan2))
		Assertions.assertArrayEquals(
			arrayOf(plan1),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 1")
	fun deleteVararg1() = runTest {
		plans.add(plan1.toPlanEntity())
		dao.delete(*arrayOf(plan1))
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 2")
	fun deleteVararg2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
		}
		dao.delete(plan1, plan2)
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(vararg Todo): delete 3")
	fun deleteVararg3() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		dao.delete(plan1, plan3)
		Assertions.assertArrayEquals(
			arrayOf(plan2),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(Todo): fail delete 1")
	fun deleteFail1() = runTest {
		val deleteResult = dao.delete(plan1)
		Assertions.assertFalse(deleteResult)
	}

	@Test
	@DisplayName("delete(Todo): fail delete 2")
	fun deleteFail2() = runTest {
		plans.add(plan2.toPlanEntity())
		val deleteResult = dao.delete(plan1)
		Assertions.assertFalse(deleteResult)
		Assertions.assertArrayEquals(
			arrayOf(plan2),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(Todo): delete 1")
	fun delete1() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
		}
		val deleteResult = dao.delete(plan1)
		Assertions.assertTrue(deleteResult)
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}

	@Test
	@DisplayName("delete(Todo): delete 2")
	fun delete2() = runTest {
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		val deleteResult = dao.delete(plan2)
		Assertions.assertTrue(deleteResult)
		Assertions.assertArrayEquals(
			arrayOf(plan1, plan3),
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
		plans.apply {
			add(plan1.toPlanEntity())
			add(plan2.toPlanEntity())
			add(plan3.toPlanEntity())
		}
		dao.deleteAll()
		Assertions.assertArrayEquals(
			arrayOf(),
			tasksArray
		)
	}
	
}