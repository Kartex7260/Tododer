package kanti.tododer.ui.screens.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.common.Const
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.toUiState
import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.FullIds
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.domain.plan.planwithchildren.GetPlanWithChildrenUseCase
import kanti.tododer.domain.task.taskwithchildren.GetTaskWithChildrenUseCase
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

private typealias Tag = Const.LogTag

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
	private val getTaskWithChildren: GetTaskWithChildrenUseCase,
	private val getPlanWithChildren: GetPlanWithChildrenUseCase,
	private val taskRepository: TaskRepository
) : ViewModel() {

	private val stack = Stack<FullId>()
	private var currentFullId: FullId? = null

	private val _todoDetailLiveData = MutableLiveData<TodoDetailUiState>()
	val todoDetailLiveData: LiveData<TodoDetailUiState> = _todoDetailLiveData

	fun taskIsDone(task: Task, isDone: Boolean, callback: MutableLiveData<UiState<Task>>) {
		viewModelScope.launch {
			val taskRepositoryResult = taskRepository.replace(task) {
				copy(
					done = isDone
				)
			}
			callback.postValue(taskRepositoryResult.toUiState(Task()))
		}
	}

	fun pop() {
		_todoDetailLiveData.value = TodoDetailUiState(process = true)
		viewModelScope.launch {
			try {
				currentFullId = stack.pop()
				showTodo(currentFullId!!)
			} catch (th: EmptyStackException) {
				_todoDetailLiveData.postValue(
					TodoDetailUiState(
					type = TodoDetailUiState.Type.EmptyStack
				)
				)
			} catch (th: Throwable) {
				_todoDetailLiveData.postValue(
					TodoDetailUiState(
						type = TodoDetailUiState.Type.Fail(th.message)
					)
				)
			}
		}
	}

	fun showTodo(fullId: String) {
		Log.d(Tag.METHOD, "showTodo(String = \"$fullId\")")
		_todoDetailLiveData.value = TodoDetailUiState(process = true)
		viewModelScope.launch {
			Log.d(Tag.BUSINESS_LOGIC, "showTodo(String = \"$fullId\"): coroutine start")
			val parsedFullId = FullIds.parseFullId(fullId)
			Log.d(Tag.BUSINESS_LOGIC, "showTodo(String = \"$fullId\"): parsedFullId = $parsedFullId")
			if (parsedFullId == null) {
				_todoDetailLiveData.postValue(
					TodoDetailUiState(
						type = TodoDetailUiState.Type.InvalidFullId(fullId)
					)
				)
				return@launch
			}

			val success = showTodo(parsedFullId)
			if (success) {
				Log.d(Tag.BUSINESS_LOGIC, "showTodo(String = \"$fullId\"): showTodo(FullId) return success response")
				currentFullId?.apply {
					Log.d(Tag.BUSINESS_LOGIC, "showTodo(String = \"$fullId\"): add currentFullId($currentFullId) to stack")
					stack.push(this)
				}
				currentFullId = parsedFullId
			}
		}
	}

	private suspend fun showTodo(fullId: FullId): Boolean {
		Log.d(Tag.METHOD, "showTodo(FullId = \"$fullId\")")
		val uiState = when (fullId.type) {
			Todo.Type.PLAN -> getPlan(fullId.id)
			Todo.Type.TASK -> getTask(fullId.id)
		}
		Log.d(Tag.UI_STATE, "showTodo(FullId = \"$fullId\"): get ui state ($uiState)")
		_todoDetailLiveData.postValue(uiState)
		return uiState.isSuccess
	}

	private suspend fun getTask(id: Int): TodoDetailUiState {
		Log.d(Tag.METHOD, "getTask(Int = \"$id\")")
		val repositoryResult = getTaskWithChildren(id)
		Log.d(Tag.BUSINESS_LOGIC, "getTask(Int = \"$id\"): gotten task with children (${repositoryResult.value})")
		return repositoryResult.toTaskTodoDetailUiState
	}

	private suspend fun getPlan(id: Int): TodoDetailUiState {
		Log.d(Tag.METHOD, "getPlan(Int = \"$id\")")
		val repositoryResult = getPlanWithChildren(id)
		Log.d(Tag.BUSINESS_LOGIC, "getPlan(Int = \"$id\"): gotten plan with children (${repositoryResult.value})")
		return repositoryResult.toPlanTodoDetailUiState
	}

}