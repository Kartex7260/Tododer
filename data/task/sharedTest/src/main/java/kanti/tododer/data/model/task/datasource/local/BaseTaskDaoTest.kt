package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.datasource.local.TaskDao
import kotlinx.coroutines.test.runTest

class BaseTaskDaoTest(
	private val taskDao: kanti.tododer.data.model.task.datasource.local.TaskDao
) : TaskDaoTest {



	override fun afterEach() = runTest {
		taskDao.deleteAll()
	}

	override fun getAll1() {
		TODO("Not yet implemented")
	}

	override fun getAll2() {
		TODO("Not yet implemented")
	}

	override fun getChildren1() {
		TODO("Not yet implemented")
	}

	override fun getChildren2() {
		TODO("Not yet implemented")
	}

	override fun getByRowId1() {
		TODO("Not yet implemented")
	}

	override fun getByRowId2() {
		TODO("Not yet implemented")
	}

	override fun getTask1() {
		TODO("Not yet implemented")
	}

	override fun getTask2() {
		TODO("Not yet implemented")
	}

	override fun insert1() {
		TODO("Not yet implemented")
	}

	override fun insert2() {
		TODO("Not yet implemented")
	}

	override fun delete1() {
		TODO("Not yet implemented")
	}

	override fun delete2() {
		TODO("Not yet implemented")
	}

	override fun deleteAll() {
		TODO("Not yet implemented")
	}
}