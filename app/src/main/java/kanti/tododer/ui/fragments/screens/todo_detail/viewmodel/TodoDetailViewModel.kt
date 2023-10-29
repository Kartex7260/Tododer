package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.common.logTag
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.toUiState
import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.FullIds
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toFullId
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.domain.gettodowithchildren.GetPlanWithChildrenUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.domain.gettodowithchildren.GetTaskWithChildrenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
	private val getTaskWithChildren: GetTaskWithChildrenUseCase,
	private val getPlanWithChildren: GetPlanWithChildrenUseCase,
	private val computePlanProgressUseCase: ComputePlanProgressUseCase,
	private val taskRepository: TaskRepository
) : ViewModel() {

	private val stack = Stack<FullId>()
	private var currentFullId: FullId? = null

	private val _todoDetail = MutableStateFlow<TodoDetailUiState>(TodoDetailUiState.Empty)
	val todoDetail = _todoDetail.asStateFlow()

	fun taskIsDone(task: Task, isDone: Boolean, callback: MutableLiveData<UiState<Task>>) {
		viewModelScope.launch {
			val taskRepositoryResult = taskRepository.replace(task) {
				copy(
					done = isDone
				)
			}
			callback.postValue(taskRepositoryResult.toUiState(Task.EMPTY))
		}
	}

	fun planProgressRequest(plan: Plan, callback: MutableLiveData<Float>) {
		viewModelScope.launch {
			computePlanProgressUseCase(plan, callback)
		}
	}

	fun pop() {
		_todoDetail.value = _todoDetail.value.copy(process = true)
		viewModelScope.launch {
			try {
				currentFullId = stack.pop()
				showTodo(currentFullId!!)
			} catch (th: EmptyStackException) {
				_todoDetail.value = TodoDetailUiState(
					type = TodoDetailUiState.Type.EmptyStack
				)
			} catch (th: Throwable) {
				_todoDetail.value = TodoDetailUiState(
					type = TodoDetailUiState.Type.Fail(th.message)
				)
			}
		}
	}

	fun showTodo(fullId: String) {
		fun log(mes: String = "") = Log.d(logTag, "showTodo(String = \"$fullId\"): $mes")

		_todoDetail.value = _todoDetail.value.copy(process = true)
		viewModelScope.launch {
			log(": coroutine start")
			val parsedFullId = FullIds.parseFullId(fullId)
			log("parsedFullId = $parsedFullId")
			if (parsedFullId == null) {
				_todoDetail.value = TodoDetailUiState(
					type = TodoDetailUiState.Type.InvalidFullId(fullId)
				)
				return@launch
			}

			showTodoAndAddToStack(parsedFullId)
		}
	}

	fun showTodo(todo: Todo) {
		_todoDetail.value = _todoDetail.value.copy(process = true)
		viewModelScope.launch {
			val fullId = todo.toFullId
			showTodoAndAddToStack(fullId)
		}
	}

	private suspend fun showTodoAndAddToStack(fullId: FullId) {
		fun log(mes: String = "") = Log.d(logTag, "addToStack(FullId = \"$fullId\"): $mes")

		val success = showTodo(fullId)
		if (success) {
			log("showTodo(FullId) return success response")
			currentFullId?.apply {
				log("add currentFullId=$currentFullId to stack")
				stack.push(this)
			}
			currentFullId = fullId
		}
	}

	private suspend fun showTodo(fullId: FullId): Boolean {
		val uiState = when (fullId.type) {
			Todo.Type.PLAN -> getPlan(fullId.id)
			Todo.Type.TASK -> getTask(fullId.id)
		}
		Log.d(logTag, "showTodo(FullId = \"$fullId\"): get uiState=$uiState")
		_todoDetail.value = uiState
		return uiState.isSuccess
	}

	private suspend fun getTask(id: Int): TodoDetailUiState {
		val repositoryResult = getTaskWithChildren(id)
		Log.d(logTag, "getTask(Int = \"$id\"): gotten task with children (${repositoryResult.value})")
		return repositoryResult.toTodoDetailUiState
	}

	private suspend fun getPlan(id: Int): TodoDetailUiState {
		val repositoryResult = getPlanWithChildren(id)
		Log.d(logTag, "getPlan(Int = \"$id\"): gotten plan with children (${repositoryResult.value})")
		return repositoryResult.toTodoDetailUiState
	}

}