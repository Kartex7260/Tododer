package kanti.tododer.ui.fragments.screens.todo_archive_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.getFromRoot
import kanti.tododer.di.ArchiveDataQualifier
import kanti.tododer.domain.archiving.UnarchiveTodoUseCase
import kanti.tododer.domain.removewithchildren.RemoveTodoWithProgenyUseCase
import kanti.tododer.ui.viewmodelfeatures.DeleteTodoFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveTodoListViewModel @Inject constructor(
	@ArchiveDataQualifier private val archivePlanRepository: PlanRepository,
	private val unarchiveTodoUseCase: UnarchiveTodoUseCase,
	override val removeTodoWithProgenyUseCase: RemoveTodoWithProgenyUseCase
) : ViewModel(), DeleteTodoFeature {

	override val coroutineScope: CoroutineScope
		get() = viewModelScope

	private val _archivePlans = MutableStateFlow(ArchivePlansUiState())
	val archivePlans = _archivePlans.asStateFlow()

	init {
		getArchivePlans()
	}

	fun getArchivePlans() {
		_archivePlans.value = _archivePlans.value.copy(process = true)
		viewModelScope.launch {
			val archivePlansRepositoryResult = archivePlanRepository.getFromRoot()
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