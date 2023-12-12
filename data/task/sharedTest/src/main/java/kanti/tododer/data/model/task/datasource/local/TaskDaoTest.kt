package kanti.tododer.data.model.task.datasource.local

interface TaskDaoTest {

	fun afterEach()

	fun getAll1()
	fun getAll2()

	fun getChildren1()
	fun getChildren2()

	fun getByRowId1()
	fun getByRowId2()

	fun getTask1()
	fun getTask2()

	fun insert1()
	fun insert2()

	fun delete1()
	fun delete2()

	fun deleteAll()
}