package kanti.tododer.ui.fragments.screens.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.common.features.ComputePlanProgressFeature
import kanti.tododer.common.logTag
import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.FullIds
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanImpl
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.TaskImpl
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.gettodowithchildren.GetPlanWithChildrenUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.domain.gettodowithchildren.GetTaskWithChildrenUseCase
import kanti.tododer.domain.deletetodowithchildren.DeleteTodoWithProgenyUseCase
import kanti.tododer.domain.todomove.RepositorySet
import kanti.tododer.common.features.DeleteTodoFeature
import kanti.tododer.common.features.SaveRemarkFeature
import kanti.tododer.common.features.SaveTitleFeature
import kanti.tododer.common.features.TaskIsDoneFeature
import kanti.tododer.data.model.common.toFullId
import kanti.tododer.data.model.progress.TodoProgressRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
	private val getTaskWithChildren: GetTaskWithChildrenUseCase,
	private val getPlanWithChildren: GetPlanWithChildrenUseCase,
	override val computePlanProgressUseCase: ComputePlanProgressUseCase,
	override val deleteTodoWithProgenyUseCase: DeleteTodoWithProgenyUseCase,
	@StandardDataQualifier override val taskRepository: TaskRepository,
	@StandardDataQualifier override val planRepository: PlanRepository,
	@StandardDataQualifier override val todoProgressRepository: TodoProgressRepository
) : ViewModel(), DeleteTodoFeature, SaveTitleFeature, SaveRemarkFeature, TaskIsDoneFeature,
	ComputePlanProgressFeature {

	override val coroutineScope: CoroutineScope
		get() = viewModelScope
	override val repositorySet: RepositorySet
		get() = RepositorySet(
			taskRepository,
			planRepository
		)

	private val stack = Stack<FullId>()
	private var currentFullId: FullId? = null

	private val _todoDetail = MutableStateFlow(TodoDetailUiState.Empty)
	val todoDetail = _todoDetail.asStateFlow()

	private val _newTodoCreated = MutableSharedFlow<TodoSavedUiState>()
	val newTodoCreated = _newTodoCreated.asSharedFlow()

	val currentTodoType: Todo.Type? get() {
		return currentFullId?.type
	}

	fun deleteTodo() {
		if (currentFullId == null) {
			_todoDetail.value = _todoDetail.value.copy(
				type = TodoDetailUiState.Type.EmptyStack
			)
			return
		}
		deleteTodo(currentFullId!!)
		pop()
	}

	fun createNewPlan() {
		viewModelScope.launch {
			if (currentFullId == null) {
				_newTodoCreated.emit(
					TodoSavedUiState(
						type = TodoSavedUiState.Type.NoTodoInstalled
					)
				)
			}

			val planImplFromDB = planRepository.insert(
				PlanImpl(
					parentId = currentFullId!!.fullId
				)
			)
			_newTodoCreated.emit(planImplFromDB.toTodoSavedUiState)
		}
	}

	fun createNewTask() {
		viewModelScope.launch {
			if (currentFullId == null) {
				_newTodoCreated.emit(
					TodoSavedUiState(
						type = TodoSavedUiState.Type.NoTodoInstalled
					)
				)
			}

			val taskFromDB = taskRepository.insert(
				TaskImpl(
					parentId = currentFullId!!.fullId
				)
			)
			_newTodoCreated.emit(taskFromDB.toTodoSavedUiState)
		}
	}

	fun pop() {
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

		viewModelScope.launch {
			_todoDetail.value = _todoDetail.value.copy(process = true)
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

	fun reshowTodo() {
		currentFullId?.also { fullId ->
			viewModelScope.launch {
				showTodo(fullId)
			}
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
			Todo.Type.PLAN -> getPlanWithChildren(
				repositorySet,
				fullId.id
			).toTodoDetailUiState
			Todo.Type.TASK -> getTaskWithChildren(
				taskRepository,
				fullId.id
			).toTodoDetailUiState
		}
		Log.d(logTag, "showTodo(FullId = \"$fullId\"): get uiState=$uiState")
		_todoDetail.value = uiState
		return uiState.isSuccess
	}

}