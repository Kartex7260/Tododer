package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.common.resultTryCatch
import kanti.tododer.data.common.todoLocalResultTryCatch
import kanti.tododer.data.common.todoResultTryCatch
import kanti.tododer.data.model.common.localUnexpectedByRowId
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.toTask
import javax.inject.Inject

class TaskRoomDataSource @Inject constructor(
	private val taskDao: TaskDao
) : TaskLocalDataSource {

	override suspend fun getTask(id: Int): GetLocalResult<Task> {
		return todoLocalResultTryCatch {
			val task = taskDao.getTask(id)?.toTask()
				?: return@todoLocalResultTryCatch GetLocalResult.NotFound(id.toString())
			GetLocalResult.Success(task)
		}
	}

	override suspend fun getChildren(fid: String): Result<List<Task>> {
		return todoResultTryCatch {
			val children = taskDao.getChildren(fid).map { it.toTask() }
			Result.success(children)
		}
	}

	override suspend fun insert(task: Task): Result<Task> {
		return todoResultTryCatch {
			val rowId = taskDao.insert(task.toTaskEntity())[0]
			val taskFromDB = taskDao.getByRowId(rowId)?.toTask() ?:
				return@todoResultTryCatch localUnexpectedByRowId(task, rowId)
			Result.success(taskFromDB)
		}
	}

	override suspend fun insert(vararg task: Task): Result<Unit> {
		return resultTryCatch {
			taskDao.delete(*task.map { it.toTaskEntity() }.toTypedArray())
		}
	}

	override suspend fun delete(vararg task: Task): Result<Unit> {
		return resultTryCatch {
			taskDao.delete(*task.map { it.toTaskEntity() }.toTypedArray())
		}
	}

	override suspend fun deleteAll(): Result<Unit> {
		return resultTryCatch {
			taskDao.deleteAll()
		}
	}

}