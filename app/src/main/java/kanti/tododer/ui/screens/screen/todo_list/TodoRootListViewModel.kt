package kanti.tododer.ui.screens.screen.todo_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.common.UiStateProcess
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.toUiState
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.getFromRoot
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoRootListViewModel @Inject constructor(
	private val planRepository: IPlanRepository
) : ViewModel() {

	private val _plansUiStateProcess = UiStateProcess<List<Plan>>(listOf())
	private val _plansLiveData = MutableLiveData<UiState<List<Plan>>>()
	val plansLiveData: LiveData<UiState<List<Plan>>> = _plansLiveData

	init {
		getRootPlans()
	}

	fun getRootPlans() {
		_plansLiveData.value = _plansUiStateProcess.process
		viewModelScope.launch {
			val rootPlans = planRepository.getFromRoot()
			val uiState = rootPlans.toUiState(listOf())
			_plansLiveData.postValue(uiState)
		}
	}

}