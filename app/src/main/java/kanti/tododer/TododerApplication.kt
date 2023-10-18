package kanti.tododer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kanti.tododer.common.Const
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.fullId
import kanti.tododer.data.model.plan.insertToRoot
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.fullId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TododerApplication : Application() {

	@Inject lateinit var planRepository: IPlanRepository
	@Inject lateinit var taskRepository: ITaskRepository

	override fun onCreate() {
		super.onCreate()
		Const.init(this)

		CoroutineScope(Dispatchers.Default).launch {
			val job = deleteAll()
			job.join()
			Plan(
				title = "Продукты",
				remark = "Купить"
			).addR().value?.apply {
				Plan(
					title = "Пятёрочка",
					remark = "А где она?"
				).add(this).value?.apply {
					Task(
						title = "Хлеб"
					).add(this)
					Task(
						title = "Молоко"
					).add(this)
				}
				Task(
					title = "Детский мир"
				).add(this)
				Task(
					title = "Авокадо",
					done = true
				).add(this)
				Plan(
					title = "Лента"
				).add(this).value?.apply {
					Task(
						title = "Бананы"
					)
				}
			}
			Plan(
				title = "Работа"
			).addR().value?.apply {
				Task(
					title = "Сделать то то"
				).add(this)
				Task(
					title = "Сделать сё то"
				).add(this)
			}
		}

	}

	private fun CoroutineScope.deleteAll(): Job {
		return launch {
			planRepository.deleteAll()
			taskRepository.deleteAll()
		}
	}

	private suspend fun Plan.addR(): RepositoryResult<Plan> = planRepository.insertToRoot(this)

	private suspend fun Plan.add(plan: Plan): RepositoryResult<Plan> {
		return planRepository.replace(this) {
			copy(
				parentId = plan.fullId
			)
		}
	}

	private suspend fun Task.add(plan: Plan): RepositoryResult<Task> {
		return taskRepository.replace(this) {
			copy(
				parentId = plan.fullId
			)
		}
	}

	private suspend fun Task.add(task: Task): RepositoryResult<Task> {
		return taskRepository.replace(this) {
			copy(
				parentId = task.fullId
			)
		}
	}

}