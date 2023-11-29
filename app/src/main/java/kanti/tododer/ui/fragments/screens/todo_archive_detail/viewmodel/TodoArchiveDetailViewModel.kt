package kanti.tododer.ui.fragments.screens.todo_archive_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.common.features.ComputePlanProgressFeature
import kanti.tododer.common.features.DeleteTodoFeature
import kanti.tododer.common.features.RepositorySetFeature
import kanti.tododer.common.features.SaveRemarkFeature
import kanti.tododer.common.features.SaveTitleFeature
import kanti.tododer.common.features.UnarchiveTodoFeature
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.isNull
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.FullIds
import kanti.tododer.data.model.common.toFullId
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.archiving.UnarchiveTodoUseCase
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.deletetodowithchildren.DeleteTodoWithProgenyUseCase
import kanti.tododer.domain.gettodowithchildren.GetTodoWithChildrenUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.data.model.RepositorySet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoArchiveDetailViewModel @Inject constructor(
	@ArchiveDataQualifier override val planRepository: PlanRepository,
	@ArchiveDataQualifier override val taskRepository: TaskRepository,
	@StandardDataQualifier override val todoProgressRepository: TodoProgressRepository,
	private val getTodoWithChildrenUseCase: GetTodoWithChildrenUseCase,
	override val deleteTodoWithProgenyUseCase: DeleteTodoWithProgenyUseCase,
	override val computePlanProgressUseCase: ComputePlanProgressUseCase,
	override val unarchiveTodoUseCase: UnarchiveTodoUseCase
) : ViewModel(), RepositorySetFeature, DeleteTodoFeature, ComputePlanProgressFeature,
	UnarchiveTodoFeature, SaveTitleFeature, SaveRemarkFeature {

	override val repositorySet: RepositorySet
		get() = RepositorySet(taskRepository, planRepository)
	override val coroutineScope: CoroutineScope
		get() = viewModelScope

	private val _currentTodo = MutableStateFlow(ArchiveTodoDetailUiState.Empty)
	val currentTodo = _currentTodo.asStateFlow()

	private val stack = Stack<FullId>()
	private var currentFullId: FullId? = null

	fun pushTodo(fullId: String) {
		val parsedFullId = FullIds.parseFullId(fullId)
		if (parsedFullId == null) {
			_currentTodo.value = _currentTodo.value.copy(
				type = ArchiveTodoDetailUiState.Type.InvalidFullId(fullId)
			)
			return
		}

		pushTodo(parsedFullId)
	}

	fun pushTodo(fullId: FullId) {
		viewModelScope.launch {
			val repositoryResult = todoToUiState(fullId)
			if (repositoryResult.isSuccess || !repositoryResult.isNull) {
				currentFullId?.also { fullId ->
					stack.push(fullId)
				}
				currentFullId = repositoryResult.value!!.todo!!.toFullId
			}
		}
	}

	fun reshowTodo() {
		currentFullId?.also { fullId ->
			viewModelScope.launch {
				todoToUiState(fullId)
			}
		}
	}

	fun pop() {
		try {
			currentFullId = stack.pop()
			viewModelScope.launch {
				todoToUiState(currentFullId!!)
			}
		} catch (ese: EmptyStackException) {
			_currentTodo.value = _currentTodo.value.copy(
				type = ArchiveTodoDetailUiState.Type.EmptyStack
			)
		}
	}

	private suspend fun todoToUiState(fullId: FullId): RepositoryResult<TodoWithChildren> {
		val todoWithChildren = getTodoWithChildrenUseCase(repositorySet, fullId)
		_currentTodo.value = todoWithChildren.toArchiveTodoDetailUiState
		return todoWithChildren
	}

}