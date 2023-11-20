package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TodoDataOwnerViewModel {

	val todoElement: StateFlow<TodoDataUiState>

	val updateStateView: SharedFlow<Unit>

	fun saveNewTitle(title: String)

	fun saveNewRemark(remark: String)

	fun taskIsDone(task: Task, done: Boolean)

	fun planProgressRequest(plan: Plan): LiveData<Float>

	fun setTodoDataSaveObserver(lifecycleOwner: LifecycleOwner, todoSavable: TodoSavable)

}