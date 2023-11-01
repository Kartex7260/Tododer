package kanti.tododer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kanti.tododer.common.Const
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.insertToRoot
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TododerApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		Const.init(this)
	}

}