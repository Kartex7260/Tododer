package kanti.tododer.ui.screens.screen.todo_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIds
import kanti.tododer.domain.plan.planwithchildren.GetPlanWithChildrenUseCase
import kanti.tododer.domain.task.taskwithchildren.GetTaskWithChildrenUseCase
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate.TodoDetailUiState
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate.isSuccess
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate.toPlanTodoDetailUiState
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate.toTaskTodoDetailUiState
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
	private val getTaskWithChildren: GetTaskWithChildrenUseCase,
	private val getPlanWithChildren: GetPlanWithChildrenUseCase
) : ViewModel() {

	private val todoStack = Stack<FullId>()

	private val _todoDetailLiveData = MutableLiveData<TodoDetailUiState>()
	val todoDetailLiveDataLiveData: LiveData<TodoDetailUiState> = _todoDetailLiveData

	private var currentFullId: FullId? = null

	fun pop() {
		_todoDetailLiveData.value = TodoDetailUiState(process = true)
		viewModelScope.launch {
			try {
				currentFullId = todoStack.pop()
				showTodo(currentFullId!!)
			} catch (ex: EmptyStackException) {
				_todoDetailLiveData.postValue(
					TodoDetailUiState(type = TodoDetailUiState.Type.EmptyStack)
				)
				return@launch
			}
		}
	}

	fun pushTodo(fullId: String) {
		_todoDetailLiveData.value = TodoDetailUiState(process = true)
		viewModelScope.launch {
			val parsedFullId = FullIds.parseFullId(fullId)
			if (parsedFullId == null) {
				_todoDetailLiveData.postValue(
					TodoDetailUiState(type = TodoDetailUiState.Type.InvalidFullId(fullId))
				)
				return@launch
			}

			currentFullId?.also { current ->
				todoStack.push(current)
			}
			currentFullId = parsedFullId

			showTodo(currentFullId!!)
		}
	}

	private suspend fun showTodo(fullId: FullId) {
		val uiState = when (fullId.type) {
			FullIds.Type.Task -> getTask(fullId.id)
			FullIds.Type.Plan -> getPlan(fullId.id)
		}
		if (uiState.isSuccess) {
			todoStack.push(fullId)
		}
		_todoDetailLiveData.postValue(uiState)
	}

	private suspend fun getTask(id: Int): TodoDetailUiState {
		val taskWithChildren = getTaskWithChildren(id)
		return taskWithChildren.toTaskTodoDetailUiState
	}

	private suspend fun getPlan(id: Int): TodoDetailUiState {
		val planWithChildren = getPlanWithChildren(id)
		return planWithChildren.toPlanTodoDetailUiState
	}

}