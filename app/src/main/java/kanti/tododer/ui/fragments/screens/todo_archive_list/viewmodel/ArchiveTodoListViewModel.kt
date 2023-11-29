package kanti.tododer.ui.fragments.screens.todo_archive_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.getFromRoot
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.domain.archiving.UnarchiveTodoUseCase
import kanti.tododer.domain.deletetodowithchildren.DeleteTodoWithProgenyUseCase
import kanti.tododer.domain.progress.ComputePlanProgressUseCase
import kanti.tododer.data.model.RepositorySet
import kanti.tododer.common.features.ComputePlanProgressFeature
import kanti.tododer.common.features.DeleteTodoFeature
import kanti.tododer.common.features.TaskIsDoneFeature
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.di.StandardDataQualifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveTodoListViewModel @Inject constructor(
	@ArchiveDataQualifier private val planRepository: PlanRepository,
	@ArchiveDataQualifier override val taskRepository: TaskRepository,
	private val unarchiveTodoUseCase: UnarchiveTodoUseCase,
	override val deleteTodoWithProgenyUseCase: DeleteTodoWithProgenyUseCase,
	override val computePlanProgressUseCase: ComputePlanProgressUseCase,
	@StandardDataQualifier override val todoProgressRepository: TodoProgressRepository
) : ViewModel(), DeleteTodoFeature, TaskIsDoneFeature, ComputePlanProgressFeature {

	override val coroutineScope: CoroutineScope
		get() = viewModelScope
	override val repositorySet: RepositorySet
		get() = RepositorySet(
			taskRepository,
			planRepository
		)

	private val _archivePlans = MutableStateFlow(ArchivePlansUiState())
	val archivePlans = _archivePlans.asStateFlow()

	init {
		getArchivePlans()
	}

	fun getArchivePlans() {
		_archivePlans.value = _archivePlans.value.copy(process = true)
		viewModelScope.launch {
			val archivePlansRepositoryResult = planRepository.getFromRoot()
			_archivePlans.value = ArchivePlansUiState(
				plans = archivePlansRepositoryResult.value ?: listOf(),
				type = archivePlansRepositoryResult.type
			)
		}
	}

	fun unarchiveTodo(todo: Todo) {
		viewModelScope.launch(NonCancellable) {
			unarchiveTodoUseCase(todo)
		}
	}

}