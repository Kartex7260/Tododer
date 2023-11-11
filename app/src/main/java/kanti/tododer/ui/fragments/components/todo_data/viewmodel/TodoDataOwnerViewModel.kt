package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.task.BaseTask
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TodoDataOwnerViewModel {

	val todoElement: StateFlow<TodoDataUiState>

	val updateStateView: SharedFlow<Unit>

	fun saveNewTitle(title: String)

	fun saveNewRemark(remark: String)

	fun taskIsDone(task: BaseTask, done: Boolean)

	fun planProgressRequest(plan: BasePlan): LiveData<Float>

	fun setTodoDataSaveObserver(lifecycleOwner: LifecycleOwner, todoSavable: TodoSavable)

}