package kanti.tododer.ui.screens.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.common.Const
import kanti.tododer.common.logTag
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
			callback.postValue(taskRepositoryResult.toUiState(Task.EMPTY))
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
		fun log(mes: String = "") = Log.d(logTag, "showTodo(String = \"$fullId\"): $mes")

		_todoDetailLiveData.value = TodoDetailUiState(process = true)
		viewModelScope.launch {
			log(": coroutine start")
			val parsedFullId = FullIds.parseFullId(fullId)
			log("parsedFullId = $parsedFullId")
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
				log("showTodo(FullId) return success response")
				currentFullId?.apply {
					log("add currentFullId=$currentFullId to stack")
					stack.push(this)
				}
				currentFullId = parsedFullId
			}
		}
	}

	private suspend fun showTodo(fullId: FullId): Boolean {
		val uiState = when (fullId.type) {
			Todo.Type.PLAN -> getPlan(fullId.id)
			Todo.Type.TASK -> getTask(fullId.id)
		}
		Log.d(logTag, "showTodo(FullId = \"$fullId\"): get uiState=$uiState")
		_todoDetailLiveData.postValue(uiState)
		return uiState.isSuccess
	}

	private suspend fun getTask(id: Int): TodoDetailUiState {
		val repositoryResult = getTaskWithChildren(id)
		Log.d(logTag, "getTask(Int = \"$id\"): gotten task with children (${repositoryResult.value})")
		return repositoryResult.toTaskTodoDetailUiState
	}

	private suspend fun getPlan(id: Int): TodoDetailUiState {
		val repositoryResult = getPlanWithChildren(id)
		Log.d(logTag, "getPlan(Int = \"$id\"): gotten plan with children (${repositoryResult.value})")
		return repositoryResult.toPlanTodoDetailUiState
	}

}